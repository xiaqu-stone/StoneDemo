package com.stone.templateapp.demo.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created By: sqq
 * Created Time: 2019-12-04 16:58.
 */
class DynamicProxy : InvocationHandler {
    private var mProxy: Any? = null
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {

        println("before execute, this is the DynamicProxy")

        println("args:$args")
        val any = if (args.isNullOrEmpty()) {
            method?.invoke(mProxy)
        } else {
            method?.invoke(mProxy, *args)
        }

        println("after execute, this is the DynamicProxy")
        return any
    }

    fun <T> newProxy(target: Any): T {
        this.mProxy = target
        return Proxy.newProxyInstance(this::class.java.classLoader, target.javaClass.interfaces, this) as T
    }
}


interface Subject {
    fun execute()

    fun execute(msg: String)
}

class RealSubject : Subject {
    override fun execute(msg: String) {
        println("this the RealSubject: execute message $msg")
    }

    override fun execute() {
        println("this the RealSubject: execute")
    }
}
