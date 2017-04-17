package com.zeta.codegen.framework;

import com.zeta.util.UQueue;

import java.io.*;

public abstract class C2C {

    //begin #1 Instance variables that store the values of the command-line arguments
    protected String classNamePrefix;
    protected Class inputClassObject;
    protected String inputClassName = null;
    protected String outputClassName;
    protected Class outputClassObject;
    protected String packageName;
    protected String qualifiedInputClassName = null;
    protected String qualifiedOutputClassName;
    //end #1

    //begin #2 Boolean instance variables that deal with class modifiers
    boolean isAbstract;
    protected final void setAbstract() {
        isAbstract = true;
    }
    protected final boolean isAbstract() {
        return isAbstract;
    }

    boolean isFinal;
    protected final void setFinal() {
        isFinal = true;
    }
    protected final boolean isFinal() {
        return isFinal;
    }

    boolean isInterface;
    protected final void setInterface() {
        isInterface = true;
    }
    protected final boolean isInterface() {
        return isInterface;
    }

    boolean isNotPublic;
    protected final void setNotPublic() {
        isNotPublic = true;
    }
    protected final boolean isNotPublic() {
        return isNotPublic;
    }
    //end #2


    public final Class createClass(String[] args) {
        //begin #3 Computes prefix for output class name
        classNamePrefix = generateClassNamePrefix();
        //end #3

        //begin #4 Handlers arguments to the transformation
        Args myArgs = new Args(args);
        checkAndProcessArgs(myArgs);
        if (!myArgs.complete())
            throw new C2CException("Usage: unprocessed flags: " + myArgs.toString());
        //end #4

        //begin #5 Accumulates info about imports and implemented interfaces
        UQueue itQ = generateInterfaces();
        UQueue importQ = generateImports();
        //end #5

        //begin #6 The heart of createClass is the string assignment
        String aClassString =
                (packageName == null ? "" : "package " + packageName + ";\n")
                        + (importQ.isEmpty() ? "" : "import " + importQ.toString(";\nimport ") + ";\n")
                        + getClassLevelJavadoc()
                        + (isNotPublic ? "" : "public ")
                        + (isFinal ? "final " : "")
                        + (isAbstract ? "abstract " : "")
                        + (isInterface ? " interface " : " class ") + outputClassName + "\n"
                        + (getSuperclass().equals("") ? "" : "    extends " + getSuperclass() + "\n")
                        + (itQ.isEmpty() ? "" : "    implements " + itQ.toString(", "))
                        + "{\n//============= F I E L D S ======================\n"
                        + generateFields()
                        + "\n//============= C O N S T R U C T O R S ==========\n"
                        + generateConstructors()
                        + "\n//============= M E T H O D S ====================\n"
                        + generateMethods()
                        + "\n//============= N E S T E D   C L A S S E S ======\n"
                        + generateNestedClasses()
                        + "}\n";
        //end #6
        try {
            //begin #7 Writes output class to an appropriate file and compile it
            FileWriter outputFile = new FileWriter(outputClassName + ".java");
            outputFile.write(aClassString);
            outputFile.close();

            String cp = System.getProperty("java.class.path");
            Process p =
                    Runtime.getRuntime().exec("javac -source 1.7 -classpath \""
                            + cp
                            + "\" "
                            + outputClassName
                            + ".java");
                p.waitFor();
            //end #7

            if (p.exitValue() == 0) {
                //#begin #8 Loads output class
                outputClassObject = Class.forName(qualifiedOutputClassName);
                //end #8
            } else {
                InputStream errStream = p.getErrorStream();
                for (int j = errStream.available(); j > 0; j--)
                    System.out.write(errStream.read());
                throw new C2CException("compile fails " + p.exitValue());
            }
        } catch (Exception e) {
            throw new C2CException(e);
        }
        //begin #9 Checks for conflicts with other transformations
        checkPostconditions();
        //end #9

        System.out.println(outputClassName + " compiled and loaded");
        return outputClassObject;
    }

    //begin #10 Declaration of the methods that concrete subclasses override
    abstract protected String generateFlags();
    abstract protected String generateClassNamePrefix();
    abstract protected void checkAndProcessArgs(Args args);
    abstract protected UQueue generateImports();
    abstract protected String getClassLevelJavadoc();
    abstract protected String getSuperclass();
    abstract protected UQueue generateInterfaces();
    abstract protected String generateFields();
    abstract protected String generateConstructors();
    abstract protected String generateMethods();
    abstract protected String generateNestedClasses();
    abstract protected void checkPostconditions();
    //end #10
}
