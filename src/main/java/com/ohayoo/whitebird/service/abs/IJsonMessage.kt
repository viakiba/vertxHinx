package com.ohayoo.whitebird.service.abs

import com.ohayoo.whitebird.player.model.IPlayer
import java.lang.Exception

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-05-12
 */
interface IJsonMessage : IMessage {
    /**
     * https://vertx.io/docs/vertx-core/kotlin/
     * https://vertx.io/docs/vertx-lang-kotlin-coroutines/kotlin/
     *
     * @param messageId
     * @param message
     * @param player
     * @throws CustomException
     * @throws Exception
     * @description 消息处理路由
     * @author huangpeng.12@bytedance.com
     */
    suspend fun handlerJson(messageId: Int, message: Any, player: IPlayer) {
        throw Exception("IProtoMessage#handlerJson 此方法为实现")
    }

    /**
     * @description 消息识别路由
     * @author huangpeng.12@bytedance.com
     */
    val jsonMessageRecognize: Map<Int, Class<*>>
        get() {
            throw Exception("IProtoMessage#getJsonMessageRecognize 此方法为实现 ")
        }
}