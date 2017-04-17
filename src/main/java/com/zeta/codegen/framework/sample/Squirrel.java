package com.zeta.codegen.framework.sample;

public class Squirrel {
    private String name;
    private byte priority;
    private int invCalls = 0;

    public boolean invariant() {
        invCalls++;
        getPriority();
        return true;
    }

    public int getInvCalls() {
        return invCalls;
    }

    Squirrel() {
        name = "Noname";
        priority = 0;
    }

    Squirrel(Squirrel s) {
        name = s.name;
        priority = s.priority;
    }

    public Squirrel copy() {
        Squirrel x = new Squirrel(this);
        return x;
    }

    private byte getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }
}