package com.ohayoo.whitebird.service;

import com.ohayoo.whitebird.annotate.BizServiceAnnotate
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.service.abs.BaseService
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@BizServiceAnnotate
class KtCommonService : BaseService() {

    override fun getMinMessageId(): Int {
        return 10100
    }

    override fun getMaxMessageId(): Int {
        return 10200
    }

    override fun getJsonMessageRecognize(): HashMap<Int, Class<*>> {
        val msgMap = HashMap<Int, Class<*>>()
        msgMap[10100] = MutableMap::class.java
        return msgMap
    }

    // https://vertx.io/docs/vertx-core/kotlin/
    // https://vertx.io/docs/vertx-lang-kotlin-coroutines/kotlin/
    override fun handler(messageId: Int?, message: Any, player: IPlayer) {
        GlobalScope.launch {
            println(Thread.currentThread().name)
            delay(100)
            val timerId = awaitEvent<Long> { handler ->
                GlobalContext.getVertx().setTimer(1000, handler)
            }
            player.send("xxxxxx",1)
        }
    }
}
