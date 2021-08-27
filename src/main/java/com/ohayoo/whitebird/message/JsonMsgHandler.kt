package com.ohayoo.whitebird.message

import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.ClassScanUtil
import com.ohayoo.whitebird.annotate.BizServiceAnnotate
import com.ohayoo.whitebird.compoent.JsonUtil
import com.ohayoo.whitebird.service.abs.BaseService
import java.lang.RuntimeException
import kotlin.Throws
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import com.ohayoo.whitebird.player.model.IPlayer
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

class JsonMsgHandler : MsgHandler {
    override fun init() {
        val bizServicePkgPath = GlobalContext.serverConfig().bizServicePkgPath
        msgJsonMap = ConcurrentHashMap()
        for (path in bizServicePkgPath) {
            val glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate::class.java)
            try {
                for (c in glazes) {
                    val bizService = c.getDeclaredConstructor().newInstance()
                    val bizServiceBase = bizService as BaseService
                    msgJsonMap.putAll(bizServiceBase.jsonMessageRecognize)
                    MsgHandler.bizServiceMap[bizServiceBase.javaClass] = bizServiceBase
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("biz service init fail")
            }
        }
    }

    @Throws(CustomException::class)
    fun recognizedMsg(msgId: Int, body: ByteArray): Any {
        val obj = msgJsonMap[msgId] ?: throw CustomException(CommonMessage.ErrorCode.MsgNotFoundException)
        return JsonUtil.byteArray2Obj(body, obj)
    }

    @Throws(Exception::class)
    override suspend fun handler(player: IPlayer, msgId: Int, body: ByteArray) {
        val msg = recognizedMsg(msgId, body)
        val baseService = recognizedBizService(msgId)
        baseService!!.handlerJson(msgId, msg, player)
    }

    companion object {
        var msgJsonMap: MutableMap<Int, Class<*>> = ConcurrentHashMap()
    }
}