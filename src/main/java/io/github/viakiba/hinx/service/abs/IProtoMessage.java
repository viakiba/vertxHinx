package io.github.viakiba.hinx.service.abs;

import com.google.protobuf.Message;
import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.player.model.IPlayer;

import java.util.Map;

/**
 * @createTime 2021-05-12
 */
public interface IProtoMessage extends IMessage {

    /**
     * @param messageId
     * @param message
     * @param player
     * @throws CustomException
     * @throws Exception
     * @description 消息处理路由
     */
    default void handler(Integer messageId, Message message, IPlayer player)
            throws Exception{
        throw new Exception("IProtoMessage#handler 此方法为实现 ");
    }

    /**
     * @description 消息识别路由
     */
    @Deprecated // 可以 通过 protobuf 生成的 desc 文件 进行识别 已经实现
    default Map<Integer, Message.Builder> getProtoMessageRecognize() throws Exception {
        throw new Exception("IProtoMessage#getProtoMessageRecognize 此方法为实现 ");
    }
}
