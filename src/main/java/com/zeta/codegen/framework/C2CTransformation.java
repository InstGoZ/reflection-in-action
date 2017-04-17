package com.zeta.codegen.framework;

public abstract class C2CTransformation extends C2CConstructor {

    //begin #1 Appends class name parameter
    protected String generateFlags() {
        return super.generateFlags() + " inputClassName";
    }
    //end #1

    protected void checkAndProcessArgs(Args args) {
        //begin #2 Strips away qualifier to set the input class name
        qualifiedInputClassName = args.getLast();
        int i = qualifiedInputClassName.lastIndexOf(".");
        if (i == -1)
            inputClassName = qualifiedInputClassName;
        else
            inputClassName = qualifiedInputClassName.substring(i + 1);
        //end #2
        super.checkAndProcessArgs(args);
        try {
            //begin #3 Loads the input class
            inputClassObject = Class.forName(qualifiedInputClassName);
            //end #3

            //begin #4 Eliminates primitives, array, and nested calsses as inputs
            if (inputClassObject.isArray()
                    || inputClassObject.getDeclaringClass() != null
                    || inputClassObject.isPrimitive())
                throw new C2CException("illegal class");
            //end #4
        } catch (ClassNotFoundException e) {
            throw new C2CException(e);
        }
    }
}
