package com.ohayoo.whitebird.player

import cn.hutool.core.lang.Tuple
import com.ohayoo.whitebird.compoent.KTCoroutineHelp.Companion.runDefault
import com.ohayoo.whitebird.boot.SystemServiceImpl
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.enums.NetType
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.player.enums.AttributeEnum
import com.ohayoo.whitebird.message.MsgSystemService
import com.ohayoo.whitebird.player.model.TcpPlayer
import com.ohayoo.whitebird.compoent.KTCoroutineHelp
import com.ohayoo.whitebird.player.model.WebsocketPlayer
import com.ohayoo.whitebird.player.model.HttpPlayer
import io.vertx.core.Promise
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lombok.extern.slf4j.Slf4j
import java.util.concurrent.ConcurrentHashMap

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
class PlayerSystemService : SystemServiceImpl {
    lateinit var playerMap: MutableMap<Long, IPlayer>
    override fun start() {
        val netType = GlobalContext.serverConfig().netType
        for (i in netType.indices) {
            if (netType[i] != NetType.http) {
                playerMap = ConcurrentHashMap<Long, IPlayer>()
            }
        }
    }

    override fun stop() {
        if (playerMap != null) {
        }
    }

    fun addPlayer(player: IPlayer) {
        playerMap!![player.getAttribute(AttributeEnum.key)] = player
    }

    private fun handle(player: IPlayer, msgId: Int, data: ByteArray) {
        val msgSystemService = GlobalContext.getSystemService<MsgSystemService>(MsgSystemService::class.java)
        GlobalScope.launch {
            msgSystemService.handler(player, msgId, data);
        }
    }

    fun tcpHandle(data: Buffer, tcpPlayer: TcpPlayer) {
        val tuple = readMessageInfo(data)
        val ktCoroutineHelp = tcpPlayer.getAttribute<KTCoroutineHelp>(AttributeEnum.coroutine)
        if (ktCoroutineHelp != null) {
            val msgSystemService = GlobalContext.getSystemService<MsgSystemService>(
                MsgSystemService::class.java
            )
            ktCoroutineHelp.addTask(tuple.get(0), tuple.get(1), msgSystemService.msgHandler!!)
        } else {
            runDefault(tcpPlayer, tuple.get(0), tuple.get(1))
        }
    }

    fun websocketHandle(data: Buffer, websocketPlayer: WebsocketPlayer) {
        val msgId = data.getInt(0)
        val body = data.getBytes(4, data.length())
        val ktCoroutineHelp = websocketPlayer.getAttribute<KTCoroutineHelp>(AttributeEnum.coroutine)
        if (ktCoroutineHelp != null) {
            val msgSystemService = GlobalContext.getSystemService<MsgSystemService>(
                MsgSystemService::class.java
            )
            ktCoroutineHelp.addTask(msgId, body, msgSystemService.msgHandler!!)
        } else {
            runDefault(websocketPlayer, msgId, body)
        }
    }

    fun httpHandle(req: HttpServerRequest, buffer: Buffer, httpPlayer: HttpPlayer) {
        val msgId = req.getHeader("header_message_id").toInt()
        val bytes = buffer.bytes
        // http服务 使用 worker线程
        GlobalContext.getVertx().executeBlocking { h: Promise<Any?>? -> handle(httpPlayer, msgId, bytes) }
    }

    companion object {
        @JvmStatic
        fun readMessageInfo(buffer: Buffer): Tuple {
            val allLength: Int
            val msgId: Int
            if (GlobalContext.serverConfig().byteOrder) {
                //小端模式
                allLength = buffer.getIntLE(0)
                msgId = buffer.getIntLE(4)
            } else {
                //大端模式
                allLength = buffer.getInt(0)
                msgId = buffer.getInt(4)
            }
            val bytes = buffer.getBytes(4 + 4, allLength)
            return Tuple(msgId, bytes)
        }

        @JvmStatic
        fun writeMessageInfo(buffer: Buffer, data: ByteArray, msgId: Int) {
            //长度数据 4字节 + 消息ID 4字节 ++ 数据内容长度
            val length = 4 + 4 + data.size
            if (GlobalContext.serverConfig().byteOrder) {
                //小端模式
                buffer.appendIntLE(length)
                buffer.appendIntLE(msgId)
            } else {
                //大端模式
                buffer.appendInt(length)
                buffer.appendInt(msgId)
            }
            buffer.appendBytes(data)
        }
    }
}