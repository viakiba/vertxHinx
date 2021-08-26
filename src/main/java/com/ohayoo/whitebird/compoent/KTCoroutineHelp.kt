package com.ohayoo.whitebird.compoent;

import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.message.MsgHandler
import com.ohayoo.whitebird.message.MsgSystemService
import com.ohayoo.whitebird.player.enums.AttributeEnum
import com.ohayoo.whitebird.player.model.IPlayer
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-26
 */

data class MsgData(val msgId: Int,val bytes: ByteArray,val handler: MsgHandler)

class KTCoroutineHelp {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private var channel: Channel<MsgData>
    private var player: IPlayer
    private lateinit var job: Job
    private lateinit var coroutineContextPlayer: CoroutineContext

    constructor(channel: Channel<MsgData>,  player: IPlayer) {
        this.channel = channel
        this.player = player
    }

    companion object {// 包裹范围内 属于静态方法
        var newSingleThreadContext: ExecutorCoroutineDispatcher = newFixedThreadPoolContext(10,"executeMain")
        val coroutineMap = ConcurrentHashMap<String, KTCoroutineHelp>()


        fun runDefault( player : IPlayer,  msgId: Int, data: ByteArray){
            GlobalScope.async(newSingleThreadContext) {
                val msgSystemService = GlobalContext.getSystemService<MsgSystemService>(MsgSystemService::class.java)
                msgSystemService.handler(player,msgId,data)
            }
        }

        fun createKTCoroutine(player: IPlayer){
            var ktCoroutineHelp = KTCoroutineHelp(Channel(30),player)
            coroutineMap[player.getAttribute(AttributeEnum.id)] = ktCoroutineHelp
            ktCoroutineHelp.start()
        }

    }

    fun addTask(
        msgId: Int,
        body: ByteArray,
        msgHandler: MsgHandler
    ) {
        GlobalScope.async(newSingleThreadContext) {
            channel.send(MsgData(msgId , body, msgHandler) )
        }
    }

    fun start() {
        // 启动一个协程
        job = GlobalScope.async(GlobalContext.getVertx().dispatcher()) {
            coroutineContextPlayer = coroutineContext
            for (t in channel) {
                //管道接收
                try {
                    println(Thread.currentThread().name)
                    t.handler.handler(player,t.msgId,t.bytes)
                } catch (e: CancellationException) {
                    log.error("### Coroutines cancelled!")
                    break
                } catch (ex: Exception) {
                    // 异常处理
                    log.error("### Coroutines error!", ex)
                }
            }
        }
    }

    //是否等待所有任务
    fun cancel(isWaitAllTask: Boolean) {
        channel.close() //关闭管道
        if (!isWaitAllTask) {
            job?.cancel()
        }
    }

    //协程是否还活着
    fun isActive(): Boolean {
        return job?.isActive
    }

}

