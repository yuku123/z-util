package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MemoryLeakDemoTest类。
 */
public class MemoryLeakDemoTest {

    @Test
    /**
     * testMemoryLeakDemoExists方法。
     */
    public void testMemoryLeakDemoExists() {
        MemoryLeakDemo demo = new MemoryLeakDemo();
        assertNotNull(demo);
    }
}
