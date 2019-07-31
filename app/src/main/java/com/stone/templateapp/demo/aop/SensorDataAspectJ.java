package com.stone.templateapp.demo.aop;

/**
 * Created By: sqq
 * Created Time: 2019-07-03 17:45.
 */
//@Aspect
//public class SensorDataAspectJ {
//    private static final String TAG = "SensorDataAspectJ";
//
//    @Around("execution(* *(..))")
//    public Object weaveAllMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startNanoTime = System.nanoTime();
//        Object returnObject = joinPoint.proceed();
//        long stopNanoTime = System.nanoTime();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Logs.i(TAG, "weaveAllMethod: " + String.format(Locale.CHINA, "Method:<s%> cost=s% ns", method.toGenericString(), String.valueOf(stopNanoTime - startNanoTime)));
//        return returnObject;
//    }
//}
