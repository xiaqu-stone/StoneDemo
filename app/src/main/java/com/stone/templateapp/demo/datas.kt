package com.stone.templateapp.demo

/**
 * Created By: sqq
 * Created Time: 18/7/20 下午8:05.
 */
data class Book(val id: Int, val name: String)

data class User(val id: Int, val name: String, val sex: Int)

class Person(){}
class Test(){
    fun test() {
        val kClass = Person().javaClass.kotlin
        buildString {  }
    }
}