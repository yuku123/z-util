package com.zifang.util.media.graph.qrcode.decoder;

import com.zifang.util.media.graph.qrcode.encoder.BitMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryBitmapTest {

    @Test
    public void testConstructor() {
        BitMatrix matrix = new BitMatrix(10, 10);
        matrix.set(5, 5);
        BinaryBitmap bitmap = new BinaryBitmap(matrix);
        assertNotNull(bitmap);
    }

    @Test
    public void testGetBlackMatrix() {
        BitMatrix matrix = new BitMatrix(10, 10);
        matrix.set(3, 7);
        BinaryBitmap bitmap = new BinaryBitmap(matrix);
        assertSame(matrix, bitmap.getBlackMatrix());
    }

    @Test
    public void testGetWidth() {
        BitMatrix matrix = new BitMatrix(25, 30);
        BinaryBitmap bitmap = new BinaryBitmap(matrix);
        assertEquals(25, bitmap.getWidth());
    }

    @Test
    public void testGetHeight() {
        BitMatrix matrix = new BitMatrix(25, 30);
        BinaryBitmap bitmap = new BinaryBitmap(matrix);
        assertEquals(30, bitmap.getHeight());
    }

    @Test
    public void testGetBlackMatrixReturnsCorrectBits() {
        BitMatrix matrix = new BitMatrix(8, 8);
        matrix.set(2, 2);
        matrix.set(4, 4);
        BinaryBitmap bitmap = new BinaryBitmap(matrix);

        BitMatrix result = bitmap.getBlackMatrix();
        assertTrue(result.get(2, 2));
        assertTrue(result.get(4, 4));
        assertFalse(result.get(0, 0));
    }
}
