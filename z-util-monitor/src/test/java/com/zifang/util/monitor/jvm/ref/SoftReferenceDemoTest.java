package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SoftReferenceDemoTest类。
 */
public class SoftReferenceDemoTest {

    @Test
    /**
     * testSoftReferenceDemoExists方法。
     */
    public void testSoftReferenceDemoExists() {
        SoftReferenceDemo demo = new SoftReferenceDemo();
        assertNotNull(demo);
    }
}
