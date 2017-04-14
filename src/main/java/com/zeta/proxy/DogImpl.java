package com.zeta.proxy;

public class DogImpl implements Dog {
    private int x = 0;
    private int y = 0;

    public void initialize(String name, int size) {

    }

    public int method1() {
        x++;
        y--;
        return 0;
    }

    public int method2() {
        x++;
        y++;
        System.out.println("Dog(" + x + "," + y + ")");
        return 0;
    }
}

