package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

public class WeakReferenceDemoTest {

    @Test
    public void testWeakReferenceDemoExists() {
        WeakReferenceDemo demo = new WeakReferenceDemo();
        assertNotNull(demo);
    }
}
