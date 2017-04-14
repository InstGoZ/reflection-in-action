package com.zeta.classloader.replace;

import java.util.*;

import com.zeta.classloader.SimpleClassLoader;

import java.lang.reflect.*;
import java.lang.ref.*;

abstract public class AbstractProduct implements Product {

    private static String directory = null;
    private static Class<?> implClass;
    private static List<WeakReference<Product>> instances = new ArrayList<>();

    public static Product newInstance() throws InstantiationException, IllegalAccessException {
        AbstractProduct obj = (AbstractProduct) implClass.newInstance();
        Product anAProxy = ProductIH.newInstance(obj);
        instances.add(new WeakReference<>(anAProxy));
        return anAProxy;
    }

    public static void reload(String dir) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        ClassLoader cl = new SimpleClassLoader(dir);
        implClass = cl.loadClass("ProductImpl");
        if (directory == null) {
            directory = dir;
            return;
        }
        directory = dir;
        List<WeakReference<Product>> newInstances = new ArrayList<>();

        Method evolve = implClass.getDeclaredMethod("evolve", Object.class);

        for (WeakReference<Product> instance : instances) {
            Proxy x = (Proxy) instance.get();
            if (x != null) {
                ProductIH aih = (ProductIH) Proxy.getInvocationHandler(x);
                Product oldObject = aih.getTarget();
                Product replacement = (Product) evolve.invoke(null, oldObject);
                aih.setTarget(replacement);
                newInstances.add(new WeakReference<>((Product) x));
            }
        }
        instances = newInstances;
    }
}