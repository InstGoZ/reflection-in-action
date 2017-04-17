package com.zeta.codegen.framework.sample;

import java.util.Vector;
import java.lang.ref.*;

public class ExtentManagedSquirrel
        extends Squirrel {
    //============= F I E L D S ======================
    static private Vector<WeakReference<ExtentManagedSquirrel>> myExtent = new Vector<>();

    //============= C O N S T R U C T O R S ==========
    public ExtentManagedSquirrel(Squirrel p0) {
        super(p0);
        myExtent.add(new WeakReference<>(this));
    }

    public ExtentManagedSquirrel() {
        super();
        myExtent.add(new WeakReference<>(this));
    }

    //============= M E T H O D S ====================
    static public ExtentManagedSquirrel[] getExtent() {
        Vector<ExtentManagedSquirrel> extent = new Vector<>();
        for (int i = myExtent.size() - 1, j = 0; i >= 0; i--) {
            ExtentManagedSquirrel anObj = myExtent.elementAt(i).get();
            if (anObj != null)
                extent.add(anObj);
            else
                myExtent.remove(i);
        }
        return extent.toArray(new ExtentManagedSquirrel[1]);
    }

//============= N E S T E D   C L A S S E S ======
}
