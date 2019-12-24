package com.stone.templateapp.demo.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created By: sqq
 * Created Time: 2019-12-05 17:38.
 */
public class CglibProxyJavaFactory implements MethodInterceptor {

    Object target;

    public CglibProxyJavaFactory(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        System.out.println("before execute, method name is " + method.getName());

        Object invoke = method.invoke(target, args);

        System.out.println("after execute, method name is " + method.getName());

        return invoke;
    }


    public <T> T getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return ((T) enhancer.create());
    }

}


