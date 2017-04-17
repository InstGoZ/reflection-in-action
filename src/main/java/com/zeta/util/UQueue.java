package com.zeta.util;

import java.util.*;
import java.lang.reflect.*;

public class UQueue {

    private List myList = new ArrayList();
    private Object eltArray;
    private Class eltType;
    private Method equalsMethod = null;

    public UQueue(Class eltType) {
        this.eltType = eltType;
        eltArray = Array.newInstance(eltType, 0);
    }

    public UQueue(Class eltType, Method m) {
        Class[] fpl = m.getParameterTypes();
        if (!(Modifier.isStatic(m.getModifiers())
                && m.getReturnType() == boolean.class
                && fpl[0] == eltType
                && fpl[1] == eltType))
            throw new RuntimeException("illegal signature");
        equalsMethod = m;
        this.eltType = eltType;
        eltArray = Array.newInstance(eltType, 0);
    }


    public boolean isEmpty() {
        return myList.size() == 0;
    }

    public int size() {
        return myList.size();
    }

    public Object remove() {
        return myList.remove(0);
    }

    public Object elementAt(int i) {
        return myList.get(i);
    }

    public UQueue add(Object element) {
        if (!eltType.isInstance(element))
            throw new RuntimeException("illegal arg type");
        if (!contains(element))
            myList.add(element);
        return this;
    }

    public boolean contains(Object obj) {
        if (equalsMethod == null) {
            return myList.contains(obj);
        } else {
            for (Object aMyList : myList) {
                try {
                    Object[] apl = {obj, aMyList};
                    Boolean rv = (Boolean) equalsMethod.invoke(obj, apl);
                    if (rv)
                        return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }
    }

    public Object[] toArray() {
        return myList.toArray((Object[]) eltArray);
    }

    public String toString(String separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < myList.size(); i++) {
            result.append(myList.get(i));
            if (i < myList.size() - 1)
                result.append(separator);
        }
        return result.toString();
    }
}
