package com.zifang.util.media.graph.image.GIF;

import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * EncoderTest类。
 */
public class EncoderTest {

    @Test
    /**
     * testEncoderExists方法。
     */
    public void testEncoderExists() {
        // Encoder class has package-private constructor with args
        // Just verify the class is accessible
        assertNotNull(Encoder.class);
    }
}
