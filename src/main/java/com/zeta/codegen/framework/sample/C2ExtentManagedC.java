package com.zeta.codegen.framework.sample;

import com.zeta.codegen.framework.Args;
import com.zeta.codegen.framework.C2CException;
import com.zeta.codegen.framework.C2CTransformation;
import com.zeta.util.Mopex;
import com.zeta.util.UQueue;

import java.lang.reflect.*;
import java.io.Serializable;

public class C2ExtentManagedC extends C2CTransformation {

    private int numberOfConstructors = 0;

    //#1 Creates a transformation objects and calls createClass
    public static void main(String[] args) {
        new C2ExtentManagedC().createClass(args);
    }

    //#2 Adds required packages to queue
    protected UQueue generateImports() {
        return super.generateImports()
                .add("java.util.Vector")
                .add("java.lang.ref.*");
    }

    //#3 Prepends the property name
    protected String generateClassNamePrefix() {
        return "ExtentManaged" + super.generateClassNamePrefix();
    }

    //#4
    protected String getSuperclass() {
        return inputClassName;
    }

    protected void checkAndProcessArgs(Args args) {
        super.checkAndProcessArgs(args);
        //begin #5 Rejects Cloneable or Serializable input classes
        if (Serializable.class.isAssignableFrom(inputClassObject))
            throw new C2CException("cannot handle Serializable input classes");
        if (Cloneable.class.isAssignableFrom(inputClassObject))
            throw new C2CException("Cloneable and Singleton are conflicting");
        //end #5
    }

    //#6 Adds static field to track the extent
    protected String generateFields() {
        return super.generateFields()
                + "    static private Vector myExtent = new Vector();\n";
    }

    //#7 Generates constructors that add to the extent
    protected String generateConstructors() {
        String managementCode =
                "    myExtent.add( new WeakReference(this) );\n";
        StringBuilder overriddenConstructors = new StringBuilder();
        Constructor[] cArray = inputClassObject.getDeclaredConstructors();
        if (cArray.length != 0) {
            for (Constructor aCArray : cArray)
                overriddenConstructors.append(Modifier.toString(aCArray.getModifiers())).append(" ").append(Mopex.createRenamedConstructor(aCArray,
                        outputClassName,
                        managementCode));
            numberOfConstructors = cArray.length;
        } else {
            overriddenConstructors = new StringBuilder(outputClassName
                    + "()\n    {\n"
                    + managementCode
                    + "    }\n");
            numberOfConstructors = 1;
        }
        return super.generateConstructors() + overriddenConstructors;
    }

    //#8 Generates method to retrive the extent
    protected String generateMethods() {
        return super.generateMethods()
                + "    static public " + outputClassName + "[] getExtent() {\n"
                + "        Vector extent = new Vector();\n"
                + "        for (int i = myExtent.size()-1, j = 0; i >= 0; i--) {\n"
                + "            " + outputClassName + " anObj = \n"
                + "                (" + outputClassName + ")\n"
                + "                ((WeakReference)myExtent.elementAt(i)).get();\n"
                + "            if ( anObj != null )\n"
                + "                extent.add(anObj);\n"
                + "            else\n"
                + "                myExtent.remove(i);\n"
                + "        }\n"
                + "        return (" + outputClassName + "[])\n"
                + "               extent.toArray( new " + outputClassName + "[1]);\n"
                + "    }\n";
    }

    //#9 Ensures no other transformation has added constructors
    protected void checkPostconditions() {
        super.checkPostconditions();
        if (outputClassObject.getDeclaredConstructors().length
                != numberOfConstructors)
            throw new C2CException("non-ExtentManaged constructors added");
    }
}