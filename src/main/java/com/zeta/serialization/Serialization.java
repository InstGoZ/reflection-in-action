package com.zeta.serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.zeta.util.Mopex;

class Serialization {

    static Document serializeObject(Object source) throws Exception {
        return serializeHelper(source, new Document(new Element("serialized")), new IdentityHashMap<Object, String>());
    }

    private static Document serializeHelper(Object source, Document target, Map<Object, String> table)
            throws Exception {
        // 1.为将被序列化的对象创建一个唯一的ID
        String id = Integer.toString(table.size());
        table.put(source, id);
        Class<?> sourceclass = source.getClass();
        // 2.为对象创建一个XML元素
        Element oElt = new Element("object");
        oElt.setAttribute("class", sourceclass.getName());
        oElt.setAttribute("id", id);
        target.getRootElement().addContent(oElt);
        // 3.处理非数组对象
        if (!sourceclass.isArray()) {
            // 4.得到非静态域
            Field[] fields = Mopex.getInstanceVariables(sourceclass);
            for (Field field : fields) {
                // 5.允许非public域允许访问
                if (!Modifier.isPublic(field.getModifiers()))
                    field.setAccessible(true);
                // 6.创建新的XML元素
                // 6.1 得到域名
                Element fElt = new Element("field");
                fElt.setAttribute("name", field.getName());
                // 6.2 得到域定义类
                Class<?> declClass = field.getDeclaringClass();
                fElt.setAttribute("declaringclass", declClass.getName());
                // 6.3 得到域类型
                Class<?> fieldtype = field.getType();
                // 6.4 得到域值
                Object child = field.get(source);
                // 6.5 处理transient修饰符
                if (Modifier.isTransient(field.getModifiers())) {
                    child = null;
                }
                fElt.addContent(serializeVariable(fieldtype, child, target, table));
                oElt.addContent(fElt);
            }
        } else {
            // 7. 处理数组组件
            Class<?> componentType = sourceclass.getComponentType();
            int length = Array.getLength(source);
            oElt.setAttribute("length", Integer.toString(length));
            for (int i = 0; i < length; i++) {
                oElt.addContent(serializeVariable(componentType, Array.get(source, i), target, table));
            }
        }
        return target;
    }

    private static Element serializeVariable(Class<?> fieldtype, Object child, Document target,
                                             Map<Object, String> table) throws Exception {
        if (child == null) {
            return new Element("null");
        } else if (!fieldtype.isPrimitive()) {
            Element reference = new Element("reference");
            if (table.containsKey(child)) {
                reference.setText(table.get(child));
            } else {
                reference.setText(Integer.toString(table.size()));
                serializeHelper(child, target, table);
            }
            return reference;
        } else {
            Element value = new Element("value");
            value.setText(child.toString());
            return value;
        }
    }
}
