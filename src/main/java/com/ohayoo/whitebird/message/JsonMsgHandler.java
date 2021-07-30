package com.ohayoo.whitebird.message;

import com.ohayoo.whitebird.annotate.BizServiceAnnotate;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.compoent.JsonUtil;
import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.player.model.IPlayer;
import com.ohayoo.whitebird.service.abs.BaseService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.ohayoo.whitebird.compoent.ClassScanUtil.getClzFromPkg;

@Slf4j
public class JsonMsgHandler implements MsgHandler{

    public static Map<Integer, Class> msgJsonMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        msgJsonMap = new ConcurrentHashMap<>();
        Set<Class<?>> glazes= getClzFromPkg(GlobalContext.serverConfig().getBizServicePkgPath(), BizServiceAnnotate.class);
        try {
            for (Class<?> c:glazes) {
                Object bizService = c.getDeclaredConstructor().newInstance();
                BaseService bizServiceBase = (BaseService) bizService;
                msgJsonMap.putAll(bizServiceBase.getJsonMessageRecognize());
                bizServiceMap.put(bizServiceBase.getClass(),bizServiceBase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("biz service init fail");
        }
    }

    public Object recognizedMsg( Integer msgId, byte[] body) throws CustomException {
        Class obj = msgJsonMap.get(msgId);
        if(obj == null){
            log.error("MessageId找不到"+body);
            throw new CustomException(CommonMessage.ErrorCode.MsgNotFoundException);
        }
        return JsonUtil.byteArray2Obj(body,obj);
    }

    @Override
    public void handler(IPlayer player, Integer msgId, byte[] body) throws Exception {
        Object msg = recognizedMsg(msgId, body);
        BaseService baseService = recognizedBizService(msgId);
        baseService.handler(msgId,msg,player);
    }


}
