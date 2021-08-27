package com.ohayoo.whitebird.message

import com.ohayoo.whitebird.boot.SystemServiceImpl
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.enums.MessageType
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.exception.CustomException
import lombok.extern.slf4j.Slf4j
import java.lang.Exception

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
class MsgSystemService : SystemServiceImpl {
    @JvmField
    var msgHandler: MsgHandler? = null
    override fun start() {
        val msgType = GlobalContext.serverConfig().msgType
        if (msgType == MessageType.json) {
            msgHandler = JsonMsgHandler()
        }
        if (msgType == MessageType.proto) {
            msgHandler = ProtoMsgHandler()
        }
        msgHandler!!.init()
    }

    suspend fun handler(player: IPlayer?, msgId: Int?, body: ByteArray?) {
        try {
            msgHandler!!.handler(player!!, msgId!!, body!!)
        } catch (e: Exception) {
            if (e is CustomException) {
                return
            } else {
            }
        }
    }
}