package io.github.viakiba.hinx.message;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.UnknownFieldSet;
import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.generate.message.CommonMessage;
import io.github.viakiba.hinx.player.model.IPlayer;
import io.github.viakiba.hinx.service.abs.BaseService;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface MsgHandler {

    Map<Class, BaseService> bizServiceMap = new ConcurrentHashMap<>();

    void init();

    void handler(IPlayer player, Integer msgId, byte[] body) throws Exception;

    default BaseService recognizedBizService(Integer msgId) throws CustomException {
        Optional<BaseService> first = bizServiceMap.values().parallelStream().filter(x -> {
            Integer minMessageId = x.getMinMessageId();
            Integer maxMessageId = x.getMaxMessageId();
            if (msgId >= minMessageId && msgId < maxMessageId) {
                return true;
            }
            return false;
        }).findFirst();
        if(first.isPresent()){
            return first.get();
        }
        throw new CustomException(CommonMessage.ErrorCode.MsgNotFoundException);
    }

}
