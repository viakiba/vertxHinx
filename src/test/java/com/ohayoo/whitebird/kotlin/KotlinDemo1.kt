package com.ohayoo.whitebird.kotlin

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

//基础协程知识 起一个协程 协程作用域 协程调度器
fun main() {
//    test0()
    test1()
//    test2()
}

fun test2() {
    var vertx = Vertx.vertx()
    GlobalScope.launch(vertx.dispatcher()) { // 将会使用vertx的默认调度器
        val timerId = awaitEvent<Long> { handler ->
            vertx.setTimer(1000, handler)
        }
        println("Event fired from timer with id $timerId")
    }
    runBlocking { delay(2000L) }
    println("xxxxxxxxx")
}

/**
 *  Kotlin 协程 :
 *      解放了回调函数的层层嵌套，提高了异步代码的可读性。
 *      协程函数 可挂起 可恢复 用户态执行，奸商了内核CPU的上下文切换
 *      关键字 suspend  修饰方法
 *  启动协程的几种方式：
 *      https://www.kotlincn.net/docs/reference/coroutines/basics.html
 *      https://bytedance.feishu.cn/wiki/wikcngaHtGXa5ZuYVh39U9fbfFh#
 *
 */
private fun test1() {
//最简单的协程
//    test1_1()
//显式（以非阻塞方式）等待所启动的后台 Job 执行结束
    test1_2()
/* 结构化的并发
    面临的问题 （test1_1 与 test1_2 ）
        以 GlobalScope 的方式在最上层创建协程，假如 忘记保持对新启动的协程的引用，它还会继续运行。
        如果协程中的代码挂起了会怎么样（例如，我们错误地延迟了太长时间），如果我们启动了太多的协程并
        导致内存不足会怎么样？ 必须手动保持对所有已启动协程的引用并 join 之很容易出错。
    解决方式
        我们可以在执行操作所在的指定作用域内启动协程， 而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动。
        3   1   2   4
        尽管 coroutineScope 未结束 ，当等待内嵌 launch 时,输出 3之后 ,也会输入2 ~
 */
//    test1_3()
/*
    取消协程

 */
//    test1_4()
/*
协程超时  注意释放资源
 */
//    test1_5()

    /*
        async
     */
//    test1_6()

}

private fun test1_6(){
    runBlocking {
//        test1_6_1()
        test1_6_2()
        delay(2000L)
    }
}

private suspend fun test1_6_2() {
    coroutineScope{
        var measureTimeMillis = measureTimeMillis {
            var test16Help1 = async { test1_6_help1()}
            var test16Help2 = async { test1_6_help2() }
            println("${test16Help1.await() + test16Help2.await()}")
        }
        println("耗时 $measureTimeMillis")
    }

}

private suspend fun test1_6_1(){
    var measureTimeMillis = measureTimeMillis {
        var test16Help1 = test1_6_help1()
        var test16Help2 = test1_6_help2()
        println("${test16Help1 + test16Help2}")
    }
    println("耗时 $measureTimeMillis")
}

private suspend fun test1_6_help1():Int{
    delay(1000L)
    return 12
}
private suspend fun test1_6_help2():Int{
    delay(1000L)
    return 15
}

private fun test1_5(){
    runBlocking {
        var withTimeout = withTimeoutOrNull(100L) {
            launch {
                delay(1000L)
            }
        }
        check(withTimeout != null){
           "超时"
        }
    }
    runBlocking { delay(10000L) }
}

private fun test1_4(){
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            try {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) { // 可以被取消的计算循环
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            } finally {
                println("资源释放")
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
    }
}

private fun test1_3(){
    runBlocking {
        launch {   // 在 runBlocking 作用域中启动一个新协程
            delay(200L)
            println("1")
        }
        coroutineScope {     // 创建一个协程作用域
            launch {
                delay(500L)
                println("2")
            }
            delay(100L)
            println("3") // 这一行会在内嵌 launch 之前输出
        }
        println("4")
    }
}
private fun test1_2(){
    runBlocking (CoroutineName("main-b")){
        /*
           不指定参数则   运行在父协程的上下文中，即 runBlocking 主协程
           参数可选 ----->
                Dispatchers.Unconfined :将工作在主线程中
                        不受限：
                                调用它的线程启动了一个协程，但它仅仅只是运行到第一个挂起点。挂起后，它恢复线程中的协程，而这完全由被调用的挂起函数来决定。
                                非受限的调度器非常适用于执行不消耗 CPU 时间的任务，以及不更新局限于特定线程的任何共享数据（如UI）的协程。
                Dispatchers.Default:默认调度器

                newSingleThreadContext("MyOwnThread"): 将使它获得一个新的线程

           协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default 代表的默认调度器
                即： launch(Dispatchers.Default) { …… } 与 GlobalScope.launch { …… } 使用相同的调度器。

         */
        var launch = launch(   ) {  //
            delay(1000L)
            println("yyyy")
        }
        println("xxxx")
//        launch.cancel() //取消该作业  此取消是有条件的  https://www.kotlincn.net/docs/reference/coroutines/cancellation-and-timeouts.html#%E5%8F%96%E6%B6%88%E6%98%AF%E5%8D%8F%E4%BD%9C%E7%9A%84
        launch.join()// 等待直到子协程执行结束  显式（以非阻塞方式）等待所启动的后台 Job 执行结束
    }
}

private fun test1_1(){
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}

/**
 *  kotlin 的 内置检查实现 require  check assert
 *  https://bytedance.feishu.cn/docs/doccnBMYCuR6OqpYQdayjnSJkKg
 */
private fun test0() {
    require(false) {
        "require"
    }
    check(false) {
        "check"
    }
    assert(false) {
        "assert"
    }
}

