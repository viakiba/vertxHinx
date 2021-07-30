package com.ohayoo.whitebird.message;

import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.player.model.IPlayer;
import com.ohayoo.whitebird.service.abs.BaseService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface MsgHandler {

    Map<Class, BaseService> bizServiceMap = new ConcurrentHashMap<>();

    void init();

    void handler(IPlayer player,Integer msgId, byte[] body) throws Exception;

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
