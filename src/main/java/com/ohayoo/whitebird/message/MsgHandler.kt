package com.ohayoo.whitebird.message

import kotlin.Throws
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.service.abs.BaseService
import com.ohayoo.whitebird.generate.message.CommonMessage
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

interface MsgHandler {
    fun init()

    @Throws(CustomException::class)
    fun recognizedBizService(msgId: Int): BaseService? {
        val first = bizServiceMap.values.parallelStream().filter { x: BaseService ->
            val minMessageId = x.minMessageId
            val maxMessageId = x.maxMessageId
            if (msgId >= minMessageId!! && msgId < maxMessageId!!) {
                return@filter true
            }
            false
        }.findFirst()
        if (first.isPresent) {
            return first.get()
        }
        throw CustomException(CommonMessage.ErrorCode.MsgNotFoundException)
    }

    @Throws(Exception::class)
    suspend fun handler(player: IPlayer, msgId: Int, body: ByteArray)

    companion object {
        val bizServiceMap = mutableMapOf<Class<*>, BaseService>()
    }
}