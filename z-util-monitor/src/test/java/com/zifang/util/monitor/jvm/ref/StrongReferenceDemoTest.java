package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

public class StrongReferenceDemoTest {

    @Test
    public void testStrongReferenceDemoExists() {
        StrongReferenceDemo demo = new StrongReferenceDemo();
        assertNotNull(demo);
    }
}
