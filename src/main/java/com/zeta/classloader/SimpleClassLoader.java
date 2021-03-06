package com.zeta.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleClassLoader extends ClassLoader {
    private String[] dirs;

    public SimpleClassLoader(String path) {
        dirs = path.split(System.getProperty("path.separator"));
    }

    public SimpleClassLoader(String path, ClassLoader parent) {
        super(parent);
        dirs = path.split(System.getProperty("path.separator"));
    }

    public void extendClasspath(String path) {
        String[] exDirs = path.split(System.getProperty("path.separator"));
        String[] newDirs = new String[dirs.length + exDirs.length];
        System.arraycopy(dirs, 0, newDirs, 0, dirs.length);
        System.arraycopy(exDirs, 0, newDirs, dirs.length, exDirs.length);
        dirs = newDirs;
    }

    public synchronized Class<?> findClass(String name) throws ClassNotFoundException {
        for (String dir : dirs) {
            byte[] buf = getClassData(dir, name);
            if (buf != null) {
                return defineClass(name, buf, 0, buf.length);
            }
        }
        throw new ClassNotFoundException();
    }

    private byte[] getClassData(String directory, String name) {
        String classFile = directory + "/" + name.replace('.', '/') + ".class";
        int classSize = (new Long((new File(classFile)).length())).intValue();
        byte[] buf = new byte[classSize];
        try {
            FileInputStream filein = new FileInputStream(classFile);
            filein.read(buf);
            filein.close();
        } catch (IOException e) {
            return null;
        }
        return buf;
    }
}
