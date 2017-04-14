package com.zeta.proxy.chain;

import java.lang.reflect.*;

public class SynchronizedIH extends InvocationHandlerBase {

    public static Object createProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new SynchronizedIH(obj));
    }

    private SynchronizedIH(Object obj) {
        super(obj);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        synchronized (this.getRealTarget()) {
            System.out.println("Synchronizing...");
            result = method.invoke(nextTarget, args);
            System.out.println("... synchronizing completed");
        }
        return result;
    }
}
