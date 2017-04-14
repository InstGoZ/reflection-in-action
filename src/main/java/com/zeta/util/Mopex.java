package com.zeta.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mopex {
    /**
     *  得到所有实例域
     */
    public static Field[] getInstanceVariables(Class<?> cls) {
        List<Field> accum = new LinkedList<Field>();
        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    accum.add(field);
                }
            }
            cls = cls.getSuperclass();
        }
        Field[] retvalue = new Field[accum.size()];
        return accum.toArray(retvalue);
    }

    /**
     *  得到所有实例方法
     */
    public static Method[] getInstanceMethods(Class<?> cls) {
        List<Method> instanceMethods = new ArrayList<Method>();
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods)
                if (!Modifier.isStatic(method.getModifiers()))
                    instanceMethods.add(method);
        }
        Method[] ims = new Method[instanceMethods.size()];
        for (int j = 0; j < instanceMethods.size(); j++)
            ims[j] = instanceMethods.get(j);
        return ims;
    }

    /**
     * 查询指定名称的域
     */
    public static Field findField(Class<?> cls, String name) throws NoSuchFieldException {
        if (cls != null) {
            try {
                return cls.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return findField(cls.getSuperclass(), name);
            }
        } else {
            throw new NoSuchFieldException();
        }
    }
}
