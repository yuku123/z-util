package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiThreadTest {

    @Test
    public void testMultiThreadExists() {
        MultiThread thread = new MultiThread();
        assertNotNull(thread);
    }

    @Test
    public void testMainMethod() {
        // The main method is a demo that prints thread info
        // Just verify the class is accessible
        assertNotNull(MultiThread.class);
    }
}
