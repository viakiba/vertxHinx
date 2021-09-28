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
import com.ohayoo.whitebird.player.PlayerSystemService
import com.ohayoo.whitebird.player.model.IPlayer
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
@Slf4j
class ProtoMsgHandler : MsgHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

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
                    MsgHandler.bizServiceMap[bizServiceBase::class.java] = bizServiceBase
                }
                log.info(  " 初始化消息service ${MsgHandler.bizServiceMap.size}")
            } catch (e: Exception) {
                log.error("消息初始化异常 ",e)
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
        PlayerSystemService.printMessage(msg)
        val baseService = recognizedBizService(msgId)
        baseService!!.handlerProto(msgId, msg, player)
    }

    companion object {
        var msgProtoMap: MutableMap<Int, Message.Builder>? = null
    }
}