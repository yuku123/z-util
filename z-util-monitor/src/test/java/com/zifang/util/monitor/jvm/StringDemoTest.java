package com.zifang.util.monitor.jvm;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * StringDemoTest类。
 */
public class StringDemoTest {

    @Test
    /**
     * testStringDemoExists方法。
     */
    public void testStringDemoExists() {
        StringDemo demo = new StringDemo();
        assertNotNull(demo);
    }

    @Test
    /**
     * testTest1Method方法。
     */
    public void testTest1Method() {
        StringDemo demo = new StringDemo();
        demo.test1(); // Should not throw exception
    }
}
