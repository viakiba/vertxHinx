package com.ohayoo.whitebird.kotlin

import kotlinx.coroutines.*
import java.io.IOException

//协程异常

fun main() {
//    在 JVM 中可以重定义一个全局的异常处理者来将所有的协程通过 ServiceLoader 注册到 CoroutineExceptionHandler。
//    全局异常处理者就如同 Thread.defaultUncaughtExceptionHandler 一样，在没有更多的指定的异常处理者被注册的时候被使用。
//    excetion1()

//  取消与异常
//    只有所有子协程完成处理 抛出的异常才会被父协程处理
//    excetion2()
//    异常合并  一般规则是“取第一个异常”，因此将处理第一个异常。 在第一个异常之后发生的所有其他异常都作为被抑制的异常绑定至第一个异常。
//    excetion3()
// 异常的捕获处理与抛出
    excetion4()


}

private fun excetion4() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("顶层捕获的异常 got $exception")
        }
        val job = GlobalScope.launch(handler) {
            val inner = launch { // 该栈内的协程都将被取消
                launch {
                    launch {
                        throw IOException() // 原始异常
                    }
                }
            }
            try {
                inner.join()
            } catch (e: CancellationException) {
                println("捕获的异常 "+e.cause)
                throw e // 取消异常被重新抛出，但原始 IOException 得到了处理
            }
        }
        job.join()
    }
}


private fun excetion3() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            //   CoroutineExceptionHandler got java.io.IOException with suppressed [java.lang.ArithmeticException]
            println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
        }
        val job = GlobalScope.launch(handler) {
            launch {
                try {
                    delay(Long.MAX_VALUE) // 当另一个同级的协程因 IOException  失败时，它将被取消
                } finally {
                    throw ArithmeticException() // 第二个异常
                }
            }
            launch {
                delay(100)
                throw IOException() // 首个异常
            }
            delay(Long.MAX_VALUE)
        }
        job.join()
    }
}

private fun excetion2() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("自定义异常捕获 $exception")
        }
        val job = GlobalScope.launch(handler) {
            launch { // 第一个子协程
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    withContext(NonCancellable) {
                        println("子协程 被取消，但直到所有孩子都终止时才会处理异常")
                        delay(100)
                        println("第一个孩子完成了它的不可取消块")
                    }
                }
            }
            launch { // 第二个子协程
                delay(10)
                println("第二个孩子抛出异常")
                throw ArithmeticException()
            }
        }
        job.join()
    }
}

private fun excetion1() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("自定义异常捕获 $exception")
        }
        val job = GlobalScope.launch(handler) { // 根协程，运行在 GlobalScope 中
            throw AssertionError()
        }
        val deferred = GlobalScope.async(handler) { // 同样是根协程，但使用 async 代替了 launch
            throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
        }
        joinAll(job, deferred)
    }
}
