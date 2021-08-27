package com.ohayoo.whitebird.service;

import com.ohayoo.whitebird.annotate.BizServiceAnnotate
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.JsonUtil
import com.ohayoo.whitebird.compoent.KTCoroutineHelp
import com.ohayoo.whitebird.player.enums.AttributeEnum
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.service.abs.BaseService
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@BizServiceAnnotate
class KtCommonService(override val minMessageId: Int = 10000, override val maxMessageId: Int = 10100) : BaseService() {

    override val jsonMessageRecognize: Map<Int, Class<*>>
        get() {
            val msgMap = mutableMapOf<Int, Class<*>>()
            msgMap[10000] = MutableMap::class.java
            return msgMap
        }

    // https://vertx.io/docs/vertx-core/kotlin/
    // https://vertx.io/docs/vertx-lang-kotlin-coroutines/kotlin/
    override suspend fun handlerJson(messageId: Int, message: Any, player: IPlayer) {
        println(Thread.currentThread().name)
        val timerId = awaitEvent<Long> { handler ->
            var nextInt = Random.Default.nextLong(200)
            GlobalContext.getVertx().setTimer(nextInt, handler)
        }
        println( JsonUtil.obj2Str(message))
        player.send("1",1)
    }

}
