package com.zeta.proxy.chain;

import java.lang.reflect.*;

import com.zeta.util.Mopex;

abstract class InvocationHandlerBase implements InvocationHandler {

    Object nextTarget;
    private Object realTarget = null;

    InvocationHandlerBase(Object target) {
        nextTarget = target;
        if (nextTarget != null) {
            realTarget = findRealTarget(nextTarget);
            if (realTarget == null)
                throw new RuntimeException("findRealTarget failure");
        }
    }

    final Object getRealTarget() {
        return realTarget;
    }

    private static Object findRealTarget(Object t) {
        if (!Proxy.isProxyClass(t.getClass()))
            return t;
        InvocationHandler ih = Proxy.getInvocationHandler(t);
        if (InvocationHandlerBase.class.isInstance(ih)) {
            return ((InvocationHandlerBase) ih).getRealTarget();
        } else {
            try {
                Field f = Mopex.findField(ih.getClass(), "target");
                if (Object.class.isAssignableFrom(f.getType()) && !f.getType().isArray()) {
                    f.setAccessible(true); // suppress access checks
                    Object innerTarget = f.get(ih);
                    return findRealTarget(innerTarget);
                }
                return null;
            } catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
                return null;
            }
        }
    }
}
