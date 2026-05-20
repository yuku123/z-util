package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringDemoTest {

    @Test
    public void testStringDemoExists() {
        StringDemo demo = new StringDemo();
        assertNotNull(demo);
    }

    @Test
    public void testTest1Method() {
        StringDemo demo = new StringDemo();
        demo.test1(); // Should not throw exception
    }
}
