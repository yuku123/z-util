package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class BitsTest {

    @Test(expected = NullPointerException.class)
    public void binaryStr_withNull() {
        Bits.binaryStr(null);
    }

    @Test
    public void binaryStr_withInteger() {
        String result = Bits.binaryStr(Integer.valueOf(8));
        assertEquals("00000000000000000000000000001000", result);
    }

    @Test
    public void binaryStr_withLong() {
        String result = Bits.binaryStr(Long.valueOf(8L));
        assertEquals("0000000000000000000000000000000000000000000000000000000000001000", result);
    }

    @Test
    public void binaryStr_withOtherNumber() {
        assertEquals("1.0", Bits.binaryStr(Double.valueOf(1.0)));
    }

    @Test
    public void isOdd() {
        assertTrue(Bits.isOdd(2));
        assertTrue(Bits.isOdd(4));
        assertFalse(Bits.isOdd(1));
        assertFalse(Bits.isOdd(3));
        assertTrue(Bits.isOdd(-2));
        assertFalse(Bits.isOdd(-1));
    }

    @Test
    public void isEven() {
        assertFalse(Bits.isEven(2));
        assertFalse(Bits.isEven(4));
        assertTrue(Bits.isEven(1));
        assertTrue(Bits.isEven(3));
        assertFalse(Bits.isEven(-2));
        assertTrue(Bits.isEven(-1));
    }

    @Test
    public void avg() {
        assertEquals(3, Bits.avg(1, 5));
        assertEquals(3, Bits.avg(2, 4));
        assertEquals(5, Bits.avg(5, 5));
        assertEquals(0, Bits.avg(-1, 1));
        assertEquals(-3, Bits.avg(-5, -1));
    }

    @Test
    public void isPowFrom2() {
        assertTrue(Bits.isPowFrom2(1));
        assertTrue(Bits.isPowFrom2(2));
        assertTrue(Bits.isPowFrom2(4));
        assertTrue(Bits.isPowFrom2(8));
        assertTrue(Bits.isPowFrom2(16));
        assertFalse(Bits.isPowFrom2(0));
        assertFalse(Bits.isPowFrom2(3));
        assertFalse(Bits.isPowFrom2(5));
        assertFalse(Bits.isPowFrom2(6));
    }

    @Test
    public void abs() {
        assertEquals(1, Bits.abs(1));
        assertEquals(1, Bits.abs(-1));
        assertEquals(0, Bits.abs(0));
        assertEquals(Integer.MAX_VALUE, Bits.abs(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, Bits.abs(Integer.MIN_VALUE));
    }

    @Test
    public void mod_powOf2() {
        assertEquals(0, Bits.mod(0, 2));
        assertEquals(1, Bits.mod(1, 2));
        assertEquals(0, Bits.mod(2, 2));
        assertEquals(3, Bits.mod(3, 4));
        assertEquals(0, Bits.mod(4, 4));
        assertEquals(7, Bits.mod(7, 8));
    }

    @Test
    public void mod_nonPowOf2() {
        assertEquals(0, Bits.mod(0, 3));
        assertEquals(1, Bits.mod(1, 3));
        assertEquals(2, Bits.mod(2, 3));
        assertEquals(0, Bits.mod(3, 3));
        assertEquals(1, Bits.mod(4, 3));
    }

    @Test
    public void multipleLess() {
        assertEquals(1, Bits.multipleLess(0));
        assertEquals(1, Bits.multipleLess(1));
        assertEquals(1, Bits.multipleLess(2));
        assertEquals(2, Bits.multipleLess(3));
        assertEquals(4, Bits.multipleLess(7));
        assertEquals(4, Bits.multipleLess(8));
        assertEquals(8, Bits.multipleLess(9));
    }

    @Test
    public void multipleMore() {
        assertEquals(1, Bits.multipleMore(0));
        assertEquals(1, Bits.multipleMore(1));
        assertEquals(2, Bits.multipleMore(2));
        assertEquals(4, Bits.multipleMore(3));
        assertEquals(8, Bits.multipleMore(5));
        assertEquals(8, Bits.multipleMore(8));
        assertEquals(16, Bits.multipleMore(9));
        assertEquals(Integer.MAX_VALUE, Bits.multipleMore(Integer.MAX_VALUE));
    }

    @Test
    public void setFalse() {
        long result = Bits.setFalse(0b1111L, 0);
        assertEquals(0b1110L, result);
    }

    @Test
    public void setFalse_middleBit() {
        long result = Bits.setFalse(0b101010L, 2);
        assertEquals(0b101010L & ~(1L << 2), result);
    }

    @Test
    public void setTrue() {
        long result = Bits.setTrue(0b1110L, 0);
        assertEquals(0b1111L, result);
    }

    @Test
    public void setTrue_middleBit() {
        long result = Bits.setTrue(0b101010L, 1);
        assertEquals(0b101011L, result);
    }

    @Test
    public void getFlag() {
        assertTrue(Bits.getFlag(0b1111L, 0));
        assertTrue(Bits.getFlag(0b1111L, 1));
        assertFalse(Bits.getFlag(0b1110L, 0));
        assertFalse(Bits.getFlag(0b0111L, 3));
    }

    @Test
    public void getFlag_withByteIndex() {
        assertTrue(Bits.getFlag(0b1L, (byte) 0));
        assertFalse(Bits.getFlag(0b0L, (byte) 0));
    }

    @Test
    public void getAllTrueIndex() {
        Set<Byte> indices = Bits.getAllTrueIndex(0b1010L);
        assertEquals(2, indices.size());
        assertTrue(indices.contains((byte) 1));
        assertTrue(indices.contains((byte) 3));
    }

    @Test
    public void getAllTrueIndex_allTrue() {
        Set<Byte> indices = Bits.getAllTrueIndex(-1L);
        assertEquals(64, indices.size());
    }

    @Test
    public void getAllTrueIndex_allFalse() {
        Set<Byte> indices = Bits.getAllTrueIndex(0L);
        assertTrue(indices.isEmpty());
    }

    @Test
    public void getAllFalseIndex() {
        Set<Byte> indices = Bits.getAllFalseIndex(0b1010L);
        assertEquals(62, indices.size());
        assertFalse(indices.contains((byte) 1));
        assertFalse(indices.contains((byte) 3));
    }

    @Test
    public void getAllFalseIndex_allFalse() {
        Set<Byte> indices = Bits.getAllFalseIndex(0L);
        assertEquals(64, indices.size());
    }

    @Test
    public void getAllFalseIndex_allTrue() {
        Set<Byte> indices = Bits.getAllFalseIndex(-1L);
        assertTrue(indices.isEmpty());
    }
}
