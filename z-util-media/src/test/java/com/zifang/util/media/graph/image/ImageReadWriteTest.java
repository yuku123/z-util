package com.zifang.util.media.graph.image;

import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.Assert.*;

public class ImageReadWriteTest {

    @Test
    public void testReadFromByteArray() throws IOException {
        // Create a simple test image
        BufferedImage original = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        original.setRGB(5, 5, 0xFF0000);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(original, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // Read from byte array
        BufferedImage read = ImageReadWrite.read(imageBytes);
        assertNotNull(read);
        assertEquals(10, read.getWidth());
        assertEquals(10, read.getHeight());
    }

    @Test
    public void testInferFormat() {
        assertEquals("png", ImageReadWrite.inferFormat("image.png"));
        assertEquals("jpeg", ImageReadWrite.inferFormat("image.jpg"));
        assertEquals("gif", ImageReadWrite.inferFormat("image.gif"));
        assertEquals("bmp", ImageReadWrite.inferFormat("image.bmp"));
        assertEquals("jpeg", ImageReadWrite.inferFormat("image.JPG"));
    }

    @Test
    public void testInferFormatNoExtension() {
        assertEquals("", ImageReadWrite.inferFormat("imageWithoutExtension"));
    }

    @Test
    public void testIsSupported() {
        assertFalse(ImageReadWrite.isSupported("image.png"));
        assertFalse(ImageReadWrite.isSupported("image.jpg"));
    }

    @Test
    public void testWriteToOutputStream() throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageReadWrite.write(image, "png", baos);
        assertTrue(baos.size() > 0);
    }

    @Test
    public void testToBytes() throws IOException {
        BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        byte[] bytes = ImageReadWrite.toBytes(image);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testToBytesWithFormat() throws IOException {
        BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        byte[] pngBytes = ImageReadWrite.toBytes(image, "png");
        assertNotNull(pngBytes);
    }
}
