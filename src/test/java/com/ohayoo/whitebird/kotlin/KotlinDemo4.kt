package com.ohayoo.whitebird.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

//共享的可变状态与并发
fun main() {
//    都限制在单个线程中
    c_test_1()
//    顶层限制在单个线程中
    c_test_2()
//  协程中的替代品叫做 Mutex  互斥锁
    c_test_3()
//actor 高效实现
//    见 KotlinDemo5.kt
}

val mutex = Mutex()
fun c_test_3(){
    runBlocking {
        withContext(Dispatchers.Default) {
            massiveRun {
                // 用锁保护每次自增
                mutex.withLock {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }
}
val counterContext = newSingleThreadContext("CounterContext")
var counter = 0
fun c_test_2(){
    runBlocking {
        withContext(counterContext) {
            massiveRun {
                // 将每次自增限制在单线程上下文中
                counter++
            }
        }
        println("Counter = $counter")
    }
}


fun c_test_1(){
    runBlocking {
        withContext(Dispatchers.Default) {
            massiveRun {
                // 将每次自增限制在单线程上下文中
                withContext(counterContext) {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // 启动的协程数量
    val k = 1000 // 每个协程重复执行同一动作的次数
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


