package com.zifang.util.media.graph.image;

import org.junit.Test;
import static org.junit.Assert.*;

public class GifEncoderTest {

    @Test
    public void testGifEncoderExists() {
        // GifEncoder class is in GIF subpackage
        assertNotNull(com.zifang.util.media.graph.image.GIF.GifEncoder.class);
    }
}
