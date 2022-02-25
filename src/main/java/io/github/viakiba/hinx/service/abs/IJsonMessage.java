package io.github.viakiba.hinx.service.abs;

import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.player.model.IPlayer;

import java.util.Map;

/**
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
     */
    default void handler(Integer messageId, Object message, IPlayer player)
            throws Exception{
        throw new Exception("");
    }

    /**
     * @description 消息识别路由
     */
    default Map<Integer, Class> getJsonMessageRecognize() throws Exception {
        throw new Exception("IProtoMessage#getJsonMessageRecognize 此方法为实现 ");
    }

}
