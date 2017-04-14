package com.zeta.proxy;

import java.lang.reflect.*;

public class TracingIH implements InvocationHandler {

    public static Object createProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new TracingIH(obj));
    }

    private Object target;

    private TracingIH(Object obj) {
        target = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            System.out.println("tracing: " + method.getName() + "(...) called");
            result = method.invoke(target, args);
        } catch (InvocationTargetException e) {
            System.out.println(method.getName() + " throws " + e.getCause());
            throw e.getCause();
        }
        System.out.println("tracing: " + method.getName() + " returns");
        return result;
    }
}
