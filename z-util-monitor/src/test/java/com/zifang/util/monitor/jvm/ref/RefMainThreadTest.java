package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * RefMainThreadTest类。
 */
public class RefMainThreadTest {

    @Test
    /**
     * testRefMainThreadExists方法。
     */
    public void testRefMainThreadExists() {
        RefMainThread demo = new RefMainThread();
        assertNotNull(demo);
    }
}
