package com.ohayoo.whitebird.message;

import com.google.protobuf.Message;
import com.ohayoo.whitebird.annotate.BizServiceAnnotate;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.player.model.IPlayer;
import com.ohayoo.whitebird.service.abs.BaseService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.ohayoo.whitebird.compoent.ClassScanUtil.getClzFromPkg;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
@Slf4j
public class ProtoMsgHandler implements MsgHandler{
    public static Map<Integer, Message.Builder> msgProtoMap ;
    @Override
    public void init() {
        msgProtoMap = new ConcurrentHashMap<>();
        Set<Class<?>> glazes= getClzFromPkg(GlobalContext.serverConfig().getBizServicePkgPath(), BizServiceAnnotate.class);
        try {
            for (Class<?> c:glazes) {
                Object bizService = c.getDeclaredConstructor().newInstance();
                BaseService bizServiceBase = (BaseService) bizService;
                msgProtoMap.putAll(bizServiceBase.getProtoMessageRecognize());
                bizServiceMap.put(bizServiceBase.getClass(),bizServiceBase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("biz service init fail");
        }
    }

    public Message recognizedMsg(Integer msgId,byte[] body) throws Exception {
        Message.Builder builder = msgProtoMap.get(msgId);
        if(builder == null){
            log.error("MessageId找不到"+body);
            throw new CustomException(CommonMessage.ErrorCode.MsgNotFoundException);
        }
        Message.Builder clone = builder.clone();
        Message.Builder builder1 = clone.mergeFrom(body);
        return builder1.build();
    }

    @Override
    public void handler(IPlayer player, Integer msgId, byte[] body) throws Exception {
        Message msg = recognizedMsg(msgId, body);
        BaseService baseService = recognizedBizService(msgId);
        baseService.handler(msgId,msg,player);
    }
}
