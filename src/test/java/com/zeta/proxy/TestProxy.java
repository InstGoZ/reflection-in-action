package com.zeta.proxy;

import org.junit.Test;

public class TestProxy {

    @Test
    public void testTraceOn(){
        DogFactory dogFactory = new DogFactory("com.zeta.proxy.DogImpl", true);
        Dog dog = dogFactory.newInstance("Candy", 12);
        dog.method1();
        dog.method2();
    }

    @Test
    public void testTraceOff(){
        DogFactory dogFactory = new DogFactory("com.zeta.proxy.DogImpl", false);
        Dog dog = dogFactory.newInstance("Candy", 12);
        dog.method1();
        dog.method2();
    }

}
