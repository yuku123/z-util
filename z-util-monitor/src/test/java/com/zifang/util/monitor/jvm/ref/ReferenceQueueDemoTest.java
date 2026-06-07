package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ReferenceQueueDemoTest类。
 */
public class ReferenceQueueDemoTest {

    @Test
    /**
     * testReferenceQueueDemoExists方法。
     */
    public void testReferenceQueueDemoExists() {
        ReferenceQueueDemo demo = new ReferenceQueueDemo();
        assertNotNull(demo);
    }
}
