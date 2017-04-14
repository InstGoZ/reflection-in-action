package com.zeta.classloader;

import org.junit.Test;

public class TestSimpleClassLoader {

    @Test
    public void testSimpleClassLoader() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        SimpleClassLoader firstClassLoader = new SimpleClassLoader("D:\\JavaSpace\\IdeaProjects\\reflection-in-action\\testclasses");
        Class<?> c1 = firstClassLoader.loadClass("ConstructOnce");
        SimpleClassLoader secondClassLoader = new SimpleClassLoader("D:\\JavaSpace\\IdeaProjects\\reflection-in-action\\testclasses");
        Class<?> c2 = secondClassLoader.loadClass("ConstructOnce");

        Object xObject = c1.newInstance();
        System.out.println(xObject.getClass().getClassLoader());

        try {
            c1.newInstance();
            throw new RuntimeException("Test fails");
        } catch (IllegalStateException e) {
        }

        Object yObject = c2.newInstance();
        System.out.println(yObject.getClass().getClassLoader());
    }
}
