package io.github.viakiba.hinx.message;

import io.github.viakiba.hinx.annotate.BizServiceAnnotate;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.compoent.JsonUtil;
import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.generate.message.CommonMessage;
import io.github.viakiba.hinx.player.model.IPlayer;
import io.github.viakiba.hinx.service.abs.BaseService;
import io.github.viakiba.hinx.compoent.ClassScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.viakiba.hinx.compoent.ClassScanUtil.getClzFromPkg;

@Slf4j
public class JsonMsgHandler implements MsgHandler{

    public static Map<Integer, Class> msgJsonMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        String[] bizServicePkgPath = GlobalContext.serverConfig().getBizServicePkgPath();
        msgJsonMap = new ConcurrentHashMap<>();
        for(String path : bizServicePkgPath) {
            Set<Class<?>> glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate.class);
            try {
                for (Class<?> c : glazes) {
                    Object bizService = c.getDeclaredConstructor().newInstance();
                    BaseService bizServiceBase = (BaseService) bizService;
                    msgJsonMap.putAll(bizServiceBase.getJsonMessageRecognize());
                    bizServiceMap.put(bizServiceBase.getClass(), bizServiceBase);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("biz service init fail");
            }
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
