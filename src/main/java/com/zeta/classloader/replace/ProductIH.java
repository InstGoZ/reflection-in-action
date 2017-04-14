package com.zeta.classloader.replace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class ProductIH implements InvocationHandler {

    private Product target = null;
    static private Class<?>[] productAInterfaces = {Product.class};

    static Product newInstance(AbstractProduct obj) {
        return (Product) Proxy.newProxyInstance(obj.getClass().getClassLoader(), productAInterfaces,
                new ProductIH(obj));
    }

    private ProductIH(AbstractProduct obj) {
        target = obj;
    }

    void setTarget(Product x) {
        target = x;
    }

    Product getTarget() {
        return target;
    }

    public Object invoke(Object t, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            result = m.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
        return result;
    }
}
