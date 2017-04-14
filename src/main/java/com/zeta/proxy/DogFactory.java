package com.zeta.proxy;

class DogFactory {

    private Class<?> dogClass;
    private boolean traceIsOn = false;

    DogFactory(String className, boolean trace) {
        try {
            dogClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        traceIsOn = trace;
    }

    Dog newInstance(String name, int size) {
        try {
            Dog d = (Dog) dogClass.newInstance();
            d.initialize(name, size);
            if (traceIsOn) {
                d = (Dog) TracingIH.createProxy(d);
            }
            return d;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
