
package com.zifang.util.xml.parser;

import com.zifang.util.xml.XmlUtil;
import com.zifang.util.xml.model.*;
import org.junit.Test;

public class DebugTest {
    @Test
    public void testDebug() throws Exception {
        String xml = "<root id=\"123\" name=\"test\"/>";
        System.out.println("Input: " + xml);
        XDocument doc = XmlUtil.parse(xml);
        System.out.println("Root: " + doc.getRoot().getName());
        System.out.println("Attrs: " + doc.getRoot().getAttributes());
    }
}
