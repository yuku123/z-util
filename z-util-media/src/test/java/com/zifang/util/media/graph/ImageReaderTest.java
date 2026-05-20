package com.zifang.util.media.graph;

import org.junit.Test;
import static org.junit.Assert.*;

public class ImageReaderTest {

    @Test
    public void testImageReaderExists() {
        // ImageReader is in reader subpackage
        com.zifang.util.media.graph.reader.ImageReader reader =
            new com.zifang.util.media.graph.reader.ImageReader();
        assertNotNull(reader);
    }
}
