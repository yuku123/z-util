package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * WeakReferenceDemoTest类。
 */
public class WeakReferenceDemoTest {

    @Test
    /**
     * testWeakReferenceDemoExists方法。
     */
    public void testWeakReferenceDemoExists() {
        WeakReferenceDemo demo = new WeakReferenceDemo();
        assertNotNull(demo);
    }
}
