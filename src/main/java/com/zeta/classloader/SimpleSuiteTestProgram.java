package com.zeta.classloader;

import java.lang.reflect.Method;

public class SimpleSuiteTestProgram {
    private static Class<?>[] formals = {String[].class};
    private static Object[] actuals =  new Object[]{new String[]{""}};
    

    public static void main(String[] args) {
        try {
            for (int i = 0; ; i++) {
                ClassLoader aClassLoader = new SimpleClassLoader("testcases");
                Class<?> c = aClassLoader.loadClass("TestCase" + i);
                Method m;
                try {
                    m = c.getMethod("main", formals);
                } catch (NoSuchMethodException e) {
                    System.out.println("TestCase" + i + ": no main in test case");
                    break;
                }
                try {
                    m.invoke(null, actuals);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
        }
    }
}
