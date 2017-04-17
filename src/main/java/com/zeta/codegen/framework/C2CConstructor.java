package com.zeta.codegen.framework;
import com.zeta.util.UQueue;

public abstract class C2CConstructor extends C2C {

    private UQueue cmdLineImports;

    //begin #1 Produces a String for usage and help messages
    protected String generateFlags() {
        return "[-notpublic] [-final] [-abstract] "
                + "[[-import name]...] [-package name] [-output name]";
    }
    //end #1

    protected String generateClassNamePrefix() {
        return "";
    }

    protected void checkAndProcessArgs(Args args) {
        //begin #2 Set output class name
        outputClassName = args.getFlagValue("-output");
        if (outputClassName == null)
            if (inputClassName == null) {
                throw new C2CException("no output class name");
            } else {
                outputClassName = classNamePrefix + inputClassName;
            }
        //end #2

        //begin #3 Sets the qualified output class name
        packageName = args.getFlagValue("-package");
        if (packageName == null)
            qualifiedOutputClassName = outputClassName;
        else
            qualifiedOutputClassName = packageName + "." + outputClassName;
        //end #3

        //begin #4 Set modifiers for the output class
        isNotPublic = args.hasFlag("-notpublic");
        isFinal = args.hasFlag("-final");
        isInterface = args.hasFlag("-interface");
        isAbstract = args.hasFlag("-abstract");
        //end #4

        //begin #5 Enqueues import package name
        cmdLineImports = args.getFlagValues("-import");
        //end #5

        if (outputClassName.equals(inputClassName))
            throw new C2CException("outputClassName = inputClassName");
    }

    protected UQueue generateImports() {
        return cmdLineImports;
    }

    protected String getClassLevelJavadoc() {
        return "";
    }

    protected String getSuperclass() {
        return "";
    }

    protected UQueue generateInterfaces() {
        return new UQueue(String.class);
    }

    protected String generateFields() {
        return "";
    }

    protected String generateConstructors() {
        return "";
    }

    protected String generateMethods() {
        return "";
    }

    protected String generateNestedClasses() {
        return "";
    }

    protected void checkPostconditions() {
    }
}
