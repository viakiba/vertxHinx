package com.ohayoo.whitebird.kotlin

//kotlin 的基础知识 ~
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.*

suspend fun massiveRun1(action: suspend () -> Unit) {
    val n = 100  // 启动的协程数量
    val k = 1000 // 每个协程重复执行同个动作的次数
    val time = measureTimeMillis {
        coroutineScope { // 协程的作用域
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

// 计数器 Actor 的各种类型
sealed class CounterMsg
object IncCounter : CounterMsg() // 递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求

// 这个函数启动一个新的计数器 actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor 状态
    for (msg in channel) { // 即将到来消息的迭代器
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

fun main() {
    runBlocking {
        val counter = counterActor() // 创建该 actor
        withContext(Dispatchers.Default) {
            massiveRun1 {
                counter.send(IncCounter)
            }
        }
        // 发送一条消息以用来从一个 actor 中获取计数值
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response))
        println("Counter = ${response.await()}")
        counter.close() // 关闭该actor
    }
}

