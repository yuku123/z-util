package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryLeakDemoTest {

    @Test
    public void testMemoryLeakDemoExists() {
        MemoryLeakDemo demo = new MemoryLeakDemo();
        assertNotNull(demo);
    }
}
