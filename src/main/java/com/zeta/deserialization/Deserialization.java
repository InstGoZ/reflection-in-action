package com.zeta.deserialization;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

public class Deserialization {
    public static Object deserializeObject(Document source) throws Exception {
        List<?> objList = source.getRootElement().getChildren();
        Map<String,Object> table = new HashMap<>();
        createInstances(table, objList);
        assignFieldValues(table, objList);
        return table.get("0");
    }

    private static void createInstances(Map<String, Object> table, List<?> objList) throws Exception {
        for (Object anObjList : objList) {
            Element oElt = (Element) anObjList;
            Class<?> cls = Class.forName(oElt.getAttributeValue("class"));
            Object instance;
            if (!cls.isArray()) {
                Constructor<?> c = cls.getDeclaredConstructor();
                if (!Modifier.isPublic(c.getModifiers())) {
                    c.setAccessible(true);
                }
                instance = c.newInstance();
            } else {
                instance = Array.newInstance(cls.getComponentType(),
                        Integer.parseInt(oElt.getAttributeValue("length")));
            }
            table.put(oElt.getAttributeValue("id"), instance);
        }
    }

    private static void assignFieldValues(Map<String, Object> table, List<?> objList) throws Exception {
        for (Object anObjList : objList) {
            Element oElt = (Element) anObjList;
            Object instance = table.get(oElt.getAttributeValue("id"));
            List<?> fElts = oElt.getChildren();
            if (!instance.getClass().isArray()) {


                for (Object fElt1 : fElts) {
                    Element fElt = (Element) fElt1;
                    String className = fElt.getAttributeValue("declaringclass");
                    Class<?> fieldDC = Class.forName(className);
                    String fieldName = fElt.getAttributeValue("name");
                    Field f = fieldDC.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(f.getModifiers())) {
                        f.setAccessible(true);
                    }
                    Element vElt = (Element) fElt.getChildren().get(0);
                    f.set(instance, deserializeValue(vElt, f.getType(), table));
                }
            } else {
                Class<?> comptype = instance.getClass().getComponentType();
                for (int j = 0; j < fElts.size(); j++) {
                    Array.set(instance, j, deserializeValue((Element) fElts.get(j), comptype, table));
                }
            }
        }
    }

    private static Object deserializeValue(Element vElt, Class<?> fieldType, Map<String, Object> table) throws ClassNotFoundException {
        String valtype = vElt.getName();

        switch (valtype) {
            case "null":
                return null;
            case "reference":
                return table.get(vElt.getText());
            default:
                if (fieldType.equals(boolean.class)) {
                    if (vElt.getText().equals("true")) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } else if (fieldType.equals(byte.class)) {
                    return Byte.valueOf(vElt.getText());
                } else if (fieldType.equals(short.class)) {
                    return Short.valueOf(vElt.getText());
                } else if (fieldType.equals(int.class)) {
                    return Integer.valueOf(vElt.getText());
                } else if (fieldType.equals(long.class)) {
                    return Long.valueOf(vElt.getText());
                } else if (fieldType.equals(float.class)) {
                    return Float.valueOf(vElt.getText());
                } else if (fieldType.equals(double.class)) {
                    return Double.valueOf(vElt.getText());
                } else if (fieldType.equals(char.class)) {
                    return vElt.getText().charAt(0);
                } else {
                    return vElt.getText();
                }
        }
    }
}
