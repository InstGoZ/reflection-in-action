package com.zeta.proxy.chain;

import com.zeta.proxy.Dog;
import com.zeta.proxy.DogImpl;
import com.zeta.proxy.TracingIH;
import org.junit.Test;

public class TestProxyChain {

    @Test
    public void testProxyChain(){
        Dog dog = (Dog) SynchronizedIH.createProxy(TracingIH.createProxy(new DogImpl()));
        dog.method1();
        dog.method2();
    }
}
