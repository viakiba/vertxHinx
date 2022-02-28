package io.github.viakiba.hinx.message;

import com.google.protobuf.Message;
import io.github.viakiba.hinx.annotate.BizServiceAnnotate;
import io.github.viakiba.hinx.boot.GlobalContext;
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

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-27
 */
@Slf4j
public class ProtoMsgHandler implements MsgHandler{
    public static Map<Integer, Message.Builder> msgProtoMap ;
    @Override
    public void init() {
        msgProtoMap = new ConcurrentHashMap<>();
        String[] bizServicePkgPath = GlobalContext.serverConfig().getBizServicePkgPath();
        for (String path : bizServicePkgPath) {
            Set<Class<?>> glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate.class);
            try {
                for (Class<?> c : glazes) {
                    Object bizService = c.getDeclaredConstructor().newInstance();
                    BaseService bizServiceBase = (BaseService) bizService;
                    msgProtoMap.putAll(bizServiceBase.getProtoMessageRecognize());
                    bizServiceMap.put(bizServiceBase.getClass(), bizServiceBase);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("biz service init fail");
            }
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
