package com.ohayoo.whitebird.service.abs;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.ohayoo.whitebird.data.model.GameData;
import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.player.model.IPlayer;

import java.util.Map;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-05-12
 */
public interface IJsonMessage extends IMessage {


    /**
     * @param messageId
     * @param message
     * @param player
     * @throws CustomException
     * @throws Exception
     * @description 消息处理路由
     * @author huangpeng.12@bytedance.com
     */
    default void handler(Integer messageId, Object message, IPlayer player)
            throws Exception{
        throw new Exception("");
    }

    /**
     * @description 消息识别路由
     * @author huangpeng.12@bytedance.com
     */
    default Map<Integer, Class> getJsonMessageRecognize() throws Exception {
        throw new Exception("IProtoMessage#getJsonMessageRecognize 此方法为实现 ");
    }

}