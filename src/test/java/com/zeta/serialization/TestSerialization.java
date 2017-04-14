package com.zeta.serialization;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;

public class TestSerialization {

    @Test
    public void testZoo(){
        Animal panda1 = new Animal("Tian Tian", "male", "Ailuropoda melanoleuca", 271);
        Animal panda2 = new Animal("Mei Xiang", "female", "Ailuropoda melanoleuca", 221);
        Zoo national = new Zoo("National Zoological Park", "Washington, D.C.");

        national.add(panda1);
        national.add(panda2);

        try {
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            Document d = Serialization.serializeObject(national);
            out.output(d, System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
