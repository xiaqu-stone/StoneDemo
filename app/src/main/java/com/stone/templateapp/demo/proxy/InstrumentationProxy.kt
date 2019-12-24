package com.stone.templateapp.demo.proxy

import com.stone.log.Logs
import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method

/**
 * Created By: sqq
 * Created Time: 2019-12-05 11:46.
 */
class InstrumentationProxyFactory(private val target: Any) : MethodInterceptor {

    fun <T> getProxyInstance(): T {
        Logs.d("getProxyInstance: ${target.javaClass}")
        val enhancer = Enhancer()
        enhancer.setSuperclass(target.javaClass)
        enhancer.setCallback(this)
        return enhancer.create() as T
    }

    override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any? {
        method ?: return null
        Logs.d("intercept: $args")
        val returnVal = if (method.name == "execStartActivity" && args?.size == 7) {
            Logs.d("intercept: before startActivity ")

            val invoke = method.invoke(target, args)

            Logs.d("intercept: after startActivity ")
            invoke
        } else {

            if (args.isNullOrEmpty()) {
                method.invoke(target)
            } else {
                method.invoke(target, *args)
            }
        }

        return returnVal
    }


}