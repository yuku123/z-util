package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PhantomReferenceDemoTest类。
 */
public class PhantomReferenceDemoTest {

    @Test
    /**
     * testPhantomReferenceDemoExists方法。
     */
    public void testPhantomReferenceDemoExists() {
        PhantomReferenceDemo demo = new PhantomReferenceDemo();
        assertNotNull(demo);
    }
}
