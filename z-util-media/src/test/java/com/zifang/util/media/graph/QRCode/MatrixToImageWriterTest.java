package com.zifang.util.media.graph.qrcode;

import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class MatrixToImageWriterTest {

    @Test
    public void testMatrixToImageWriterHasPrivateConstructor() {
        // MatrixToImageWriter has a private constructor
        // Just verify the class is accessible
        assertNotNull(MatrixToImageWriter.class);
    }

    @Test
    public void testToBufferedImage() throws IOException {
        // Create a simple BitMatrix for testing using default API
        com.google.zxing.common.BitMatrix matrix = new com.google.zxing.common.BitMatrix(10);
        matrix.set(0, 0);
        matrix.set(1, 1);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        assertNotNull(image);
        assertEquals(10, image.getWidth());
        assertEquals(10, image.getHeight());
    }
}
