package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

public class PhantomReferenceDemoTest {

    @Test
    public void testPhantomReferenceDemoExists() {
        PhantomReferenceDemo demo = new PhantomReferenceDemo();
        assertNotNull(demo);
    }
}
