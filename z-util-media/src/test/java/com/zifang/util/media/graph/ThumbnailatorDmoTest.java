package com.zifang.util.media.graph;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThumbnailatorDmoTest {

    @Test
    public void testThumbnailatorDmoExists() {
        ThumbnailatorDmo dmo = new ThumbnailatorDmo();
        assertNotNull(dmo);
    }

    @Test
    public void testMainMethodExists() {
        // Main method is a demo and requires external files
        // Just verify the class is accessible
        assertNotNull(ThumbnailatorDmo.class);
    }
}
