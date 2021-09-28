package com.ohayoo.whitebird.service.abs

import com.google.protobuf.Message
import kotlin.Throws
import com.ohayoo.whitebird.player.model.IPlayer
import java.lang.Exception

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-05-12
 */
interface IProtoMessage : IMessage {
    /**
     * @param messageId
     * @param message
     * @param player
     * @throws CustomException
     * @throws Exception
     * @description 消息处理路由
     * @author huangpeng.12@bytedance.com
     */
    @Throws(Exception::class)
    suspend fun handlerProto(messageId: Int, message: Message, player: IPlayer) {
        throw Exception("IProtoMessage#handler 此方法为实现 ")
    }

    /**
     * @description 消息识别路由
     * @author huangpeng.12@bytedance.com
     */
    @get:Throws(Exception::class)
    val protoMessageRecognize: Map<Int, Message.Builder>
        get() {
            throw Exception("IProtoMessage#getProtoMessageRecognize 此方法为实现 ")
        }
}