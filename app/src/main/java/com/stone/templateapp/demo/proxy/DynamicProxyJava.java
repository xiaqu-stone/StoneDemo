package com.stone.templateapp.demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created By: sqq
 * Created Time: 2019-12-05 10:09.
 */
public class DynamicProxyJava implements InvocationHandler {

    private Object mProxy;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before execute, this is DynamicProxy " + this);

        Object invoke = method.invoke(mProxy, args);

        System.out.println("after execute, this is DynamicProxy " + this);

        return invoke;
    }

    public <T> T newProxy(Object target) {
        this.mProxy = target;
        return ((T) Proxy.newProxyInstance(Subject.class.getClassLoader(), target.getClass().getInterfaces(), this));
    }


}
