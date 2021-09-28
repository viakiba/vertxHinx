package com.ohayoo.whitebird.player

import cn.hutool.core.lang.Tuple
import com.google.protobuf.GeneratedMessage
import com.google.protobuf.Message
import com.googlecode.protobuf.format.JsonFormat
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.boot.SystemServiceImpl
import com.ohayoo.whitebird.compoent.KTCoroutineHelp
import com.ohayoo.whitebird.compoent.KTCoroutineHelp.Companion.runDefault
import com.ohayoo.whitebird.compoent.TimeUtil
import com.ohayoo.whitebird.enums.NetType
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import com.ohayoo.whitebird.message.MsgSystemService
import com.ohayoo.whitebird.player.enums.AttributeEnum
import com.ohayoo.whitebird.player.model.HttpPlayer
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.player.model.TcpPlayer
import com.ohayoo.whitebird.player.model.WebsocketPlayer
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerRequest
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
class PlayerSystemService : SystemServiceImpl,CoroutineVerticle() {
    private val log = LoggerFactory.getLogger(this::class.java)
    lateinit var playerMap: MutableMap<Long, IPlayer>

    override fun startService() {
        val netType = GlobalContext.serverConfig().netType
        for (i in netType.indices) {
            if (netType[i] != NetType.http) {
                playerMap = ConcurrentHashMap<Long, IPlayer>()
            }
        }
    }

    override fun stopService() {
        if (playerMap != null) {
        }
    }

    fun addPlayer(player: IPlayer) {
        playerMap!![player.getAttribute(AttributeEnum.key)] = player
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
        var msgIdStr = req.getHeader(CommonMessage.HeaderType.message_id.name)
        if (msgIdStr == null){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        val msgId = msgIdStr.toInt()
        req.response().putHeader(CommonMessage.HeaderType.message_id.name,msgId.toString());
        var curTimeBeginDo = TimeUtil.currentSystemTime().toString()
        req.response().putHeader(CommonMessage.HeaderType.response_server_time.name, curTimeBeginDo);
        req.response().putHeader(CommonMessage.HeaderType.collect_time.name, curTimeBeginDo);
        req.response().putHeader(CommonMessage.HeaderType.status_code.name, CommonMessage.ErrorCode.SUCCESS.number.toString());
        //预处理完成
        val bytes = buffer.bytes
        // http服务 使用 worker线程
        val msgSystemService = GlobalContext.getSystemService<MsgSystemService>(MsgSystemService::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            msgSystemService.handler(httpPlayer, msgId, bytes,true);
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private val jsonFormat = JsonFormat()

        @JvmStatic
        fun getMsgId(cls: GeneratedMessage): Int {
            val values: Collection<Any> = cls.descriptorForType.toProto().options.allFields.values
            val iterator = values.stream().iterator()
            while (iterator.hasNext()) {
                return iterator.next() as Int
            }
            return 0
        }

        @JvmStatic
        fun readMessageInfo(buffer: Buffer): Tuple {
            val allLength: Int
            val msgId: Int
            if (GlobalContext.serverConfig().isByteOrder) {
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
            if (GlobalContext.serverConfig().isByteOrder) {
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

        @JvmStatic
        fun printMessage(message: Any) {
            if (GlobalContext.serverConfig().isDebug) {
                log.info("消息 " + message.javaClass.simpleName + " " + jsonFormat.printToString(message as Message))
            }
        }
    }
}