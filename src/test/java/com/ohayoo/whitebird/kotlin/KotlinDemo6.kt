package com.ohayoo.whitebird.kotlin

//kotlin 的基础知识 ~
// https://bytedance.feishu.cn/wiki/wikcn3U7EpCtGji5DpvDlhC7prg#
// https://bytedance.feishu.cn/docs/Xr8WozEmifh4lDp6pIoA0a#
fun main() {
    //循环
    for (i in 0..3){
        println(i)
    }
    /*
     集合
        https://www.kotlincn.net/docs/reference/list-operations.html
        https://www.kotlincn.net/docs/reference/set-operations.html
     */
    var mutableListOf = mutableListOf<Int>()
    val add = mutableListOf.add(1)

    // run let with apply 的合理使用

}
// https://www.kotlincn.net/docs/reference/extensions.html
// 扩展属性
val <T> List<T>.lastIndex: Int
    get() = size - 1

fun KotlinDemo6.getName() = "Shape"

//扩展方法
fun KotlinDemo6.test1(){
    println("xxxxx")
}

class KotlinDemo6 private constructor(){
    companion object{

        //懒汉模式
        val instance1:KotlinDemo6 by lazy( mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            KotlinDemo6()
        }
        //饿汉模式
        val instance2:KotlinDemo6 = KotlinDemo6()
    }
}

