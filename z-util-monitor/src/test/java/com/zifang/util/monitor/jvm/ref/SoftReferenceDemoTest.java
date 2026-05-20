package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

public class SoftReferenceDemoTest {

    @Test
    public void testSoftReferenceDemoExists() {
        SoftReferenceDemo demo = new SoftReferenceDemo();
        assertNotNull(demo);
    }
}
