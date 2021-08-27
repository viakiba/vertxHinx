package com.ohayoo.whitebird.message

import com.google.protobuf.Message
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.ClassScanUtil
import com.ohayoo.whitebird.annotate.BizServiceAnnotate
import com.ohayoo.whitebird.service.abs.BaseService
import java.lang.RuntimeException
import kotlin.Throws
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import com.ohayoo.whitebird.player.model.IPlayer
import lombok.extern.slf4j.Slf4j
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
@Slf4j
class ProtoMsgHandler : MsgHandler {
    override fun init() {
        msgProtoMap = ConcurrentHashMap()
        val bizServicePkgPath = GlobalContext.serverConfig().bizServicePkgPath
        for (path in bizServicePkgPath) {
            val glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate::class.java)
            try {
                for (c in glazes) {
                    val bizService = c.getDeclaredConstructor().newInstance()
                    val bizServiceBase = bizService as BaseService
                    msgProtoMap!!.putAll(bizServiceBase.protoMessageRecognize)
                    MsgHandler.bizServiceMap[BizServiceAnnotate::class.java] = bizServiceBase
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("biz service init fail")
            }
        }
    }

    @Throws(Exception::class)
    fun recognizedMsg(msgId: Int, body: ByteArray): Message {
        val builder = msgProtoMap!![msgId]
        if (builder == null) {
            throw CustomException(CommonMessage.ErrorCode.MsgNotFoundException)
        }
        val clone = builder.clone()
        val builder1 = clone.mergeFrom(body)
        return builder1.build()
    }

    @Throws(Exception::class)
    override suspend fun handler(player: IPlayer, msgId: Int, body: ByteArray) {
        val msg = recognizedMsg(msgId, body)
        val baseService = recognizedBizService(msgId)
        baseService!!.handlerProto(msgId, msg, player)
    }

    companion object {
        var msgProtoMap: MutableMap<Int, Message.Builder>? = null
    }
}