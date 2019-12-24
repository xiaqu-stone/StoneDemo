package com.stone.templateapp.demo.proxy

/**
 * Created By: sqq
 * Created Time: 2019-12-05 16:04.
 */
class StaticProxy(private val target: Subject) : Subject {

    override fun execute() {
        println("before execute, in StaticProxy")
        target.execute()
        println("after execute, in StaticProxy")
    }

    override fun execute(msg: String) {
        println("before execute, in StaticProxy")
        target.execute(msg)
        println("after execute, in StaticProxy")
    }

}
