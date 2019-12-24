package com.stone.templateapp.demo.proxy

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method

/**
 * Created By: sqq
 * Created Time: 2019-12-05 16:54.
 */
class CglibProxyFactory(private val target: Any) : MethodInterceptor {


    @Throws(ClassCastException::class)
    fun <T> getProxyInstance(): T {
        val enhancer = Enhancer()
        //设置需要目标对象的类class
        enhancer.setSuperclass(target.javaClass)
        enhancer.setCallback(this)
        return enhancer.create() as T
    }

    override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any? {
//        method?:return null

        println("before execute, method name is ${method?.name}")

        val invoke = if (args.isNullOrEmpty()) {
            method?.invoke(target)
        } else {
            method?.invoke(target, *args)
        }

        println("after execute, method name is ${method?.name}")
        return invoke
    }

}


open class CglibSubject {

    fun print() {
        println("this is CglibSubject in print")
    }

    fun input(msg: String) {
        println("this is CglibSubject in input msg: $msg")
    }

}