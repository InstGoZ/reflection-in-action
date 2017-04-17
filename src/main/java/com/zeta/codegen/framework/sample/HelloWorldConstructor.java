package com.zeta.codegen.framework.sample;

import com.zeta.codegen.framework.C2CConstructor;

public class HelloWorldConstructor extends C2CConstructor {

    static public void main( String[] args ) {
		new HelloWorldConstructor().createClass( args );
    }

    protected String generateMethods() {
		return super.generateMethods()
			+ "    public static void main( String[] args ) { \n"
			+ "        System.out.println( \"Hello world!\" );\n"
			+ "    }                                          \n";
    }
}
