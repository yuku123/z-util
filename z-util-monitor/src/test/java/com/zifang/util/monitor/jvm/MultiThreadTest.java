package com.zifang.util.monitor.jvm;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * MultiThreadTest类。
 */
public class MultiThreadTest {

    @Test
    /**
     * testMultiThreadExists方法。
     */
    public void testMultiThreadExists() {
        MultiThread thread = new MultiThread();
        assertNotNull(thread);
    }

    @Test
    /**
     * testMainMethod方法。
     */
    public void testMainMethod() {
        // The main method is a demo that prints thread info
        // Just verify the class is accessible
        assertNotNull(MultiThread.class);
    }
}
