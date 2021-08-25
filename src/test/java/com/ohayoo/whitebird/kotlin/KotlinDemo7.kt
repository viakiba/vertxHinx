package com.ohayoo.whitebird.kotlin

import kotlinx.coroutines.*

fun main() {
    var counter1 = 1
    runBlocking(newFixedThreadPoolContext(10,"fix")) {
        launch {
            while (true){
//                delay(100L)
                println("${Thread.currentThread().name} --------- ${counter1++}" )
            }
        }
    }

}


