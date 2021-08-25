package com.ohayoo.whitebird.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*

//异步流 与 channel
fun main() {
    //异步流
//    f_test1()
    //channel
    c_test1()
}

fun c_test1(){
//    c_test1_0()
//    c_test1_1()
    c_test1_2()
}

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

fun c_test1_2(){
    runBlocking {
        val squares = produceSquares()
        squares.consumeEach { println(it) }
        println("Done!")
    }
}

fun c_test1_1(){
//迭代关闭通道
    runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close() // 我们结束发送
        }
        // 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
        for (y in channel) println(y)
        println("Done!")
    }
}

fun c_test1_0(){
    //基础实现
    runBlocking {
        val channel = Channel<Int>()
        launch {
            // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
            for (x in 1..5) channel.send(x * x)
        }
        // 这里我们打印了 5 次被接收的整数：
        repeat(5) { println(channel.receive()) }
        println("Done!")
    }
}


fun f_test1(){
//    simple0().forEach { v-> println(v) }

//    runBlocking {
//        simple1().forEach { v-> println(v) }
//    }

    runBlocking {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        //每次都会触发流的执行  类似 java 的 stream 操作 也类似 rxjava
        withTimeoutOrNull(2500L){//这里可以取消flow
            simple2()
//                .transform { v->emit("xxxx $v") } //转换
//                .take(2)  //限制长度
                .collect { v -> println(v) }
        }
    }

}

fun simple2():Flow<Int> = flow{
    for (i in 1..3) {
        delay(1000) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}

//使用 List 结果类型，意味着我们只能一次返回所有值。
suspend fun simple1():List<Int>{
    delay(1000L)
    return listOf(1,2,3)
}

fun simple0():Sequence<Int> = sequence{
    for (i in 1..3) {
        Thread.sleep(1000) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

