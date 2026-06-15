package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * StrongReferenceDemoTest类。
 */
public class StrongReferenceDemoTest {

    @Test
    /**
     * testStrongReferenceDemoExists方法。
     */
    public void testStrongReferenceDemoExists() {
        StrongReferenceDemo demo = new StrongReferenceDemo();
        assertNotNull(demo);
    }
}
