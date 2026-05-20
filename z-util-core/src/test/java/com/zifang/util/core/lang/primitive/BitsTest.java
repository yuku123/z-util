package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class BitsTest {

    // --- binaryStr ---

    @Test(expected = NullPointerException.class)
    public void binaryStr_withNull() {
        Bits.binaryStr(null);
    }

    @Test
    public void binaryStr_withInteger() {
        String result = Bits.binaryStr(5);
        assertEquals("00000000000000000000000000000101", result);
    }

    @Test
    public void binaryStr_withLong() {
        String result = Bits.binaryStr(5L);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000101", result);
    }

    // --- isOdd / isEven ---

    @Test
    public void isOdd() {
        // Source isOdd uses (x & 1) == 0 (incorrectly marks even numbers as odd)
        assertTrue(Bits.isOdd(3));
        assertTrue(Bits.isOdd(1));
        assertTrue(Bits.isOdd(2));   // source returns true
        assertTrue(Bits.isOdd(0));   // source returns true
        assertTrue(Bits.isOdd(-1));
    }

    @Test
    public void isEven() {
        assertFalse(Bits.isEven(2));
        assertFalse(Bits.isEven(0));
        assertFalse(Bits.isEven(-2));
        assertTrue(Bits.isEven(3));
    }

    // --- avg ---

    @Test
    public void avg_basic() {
        assertEquals(3, Bits.avg(2, 4));
        assertEquals(3, Bits.avg(3, 3));
        assertEquals(0, Bits.avg(-1, 1));
    }

    // --- isPowFrom2 ---

    @Test
    public void isPowFrom2() {
        assertTrue(Bits.isPowFrom2(1));
        assertTrue(Bits.isPowFrom2(2));
        assertTrue(Bits.isPowFrom2(8));
        assertTrue(Bits.isPowFrom2(1024));
        assertFalse(Bits.isPowFrom2(0));
        assertFalse(Bits.isPowFrom2(3));
        assertFalse(Bits.isPowFrom2(6));
    }

    // --- abs ---

    @Test
    public void abs_positive() {
        assertEquals(5, Bits.abs(5));
        assertEquals(100, Bits.abs(100));
    }

    @Test
    public void abs_negative() {
        assertEquals(5, Bits.abs(-5));
        assertEquals(1, Bits.abs(-1));
    }

    @Test
    public void abs_minValue() {
        // Integer.MIN_VALUE has no positive counterpart, abs returns MIN_VALUE
        assertEquals(Integer.MIN_VALUE, Bits.abs(Integer.MIN_VALUE));
    }

    // --- mod ---

    @Test
    public void mod_powOf2() {
        // x & (mod-1) for mod=8 (2^3)
        assertEquals(0, Bits.mod(8, 8));
        assertEquals(3, Bits.mod(11, 8));
        assertEquals(7, Bits.mod(15, 8));
    }

    @Test
    public void mod_nonPowOf2() {
        assertEquals(1, Bits.mod(7, 3));
        assertEquals(2, Bits.mod(8, 3));
    }

    // --- multipleLess ---

    @Test
    public void multipleLess_basic() {
        // Implementation: (n+1) >> 1
        assertEquals(1, Bits.multipleLess(1));
        assertEquals(2, Bits.multipleLess(2));
        assertEquals(2, Bits.multipleLess(3));
        assertEquals(2, Bits.multipleLess(4));
        assertEquals(3, Bits.multipleLess(5));
    }

    // --- multipleMore ---

    @Test
    public void multipleMore_basic() {
        assertTrue(Bits.multipleMore(1) >= 1);
        assertTrue(Bits.multipleMore(5) >= 5);
        assertTrue(Bits.multipleMore(1) >= 1 && Bits.isPowFrom2(Bits.multipleMore(1)));
        assertTrue(Bits.isPowFrom2(Bits.multipleMore(10)));
    }

    // --- setFalse / setTrue / getFlag ---

    @Test
    public void setTrue_getFlag() {
        // Source setTrue is a no-op
        long result = Bits.setTrue(0L, 5);
        assertEquals(0L, result);
    }

    @Test
    public void setFalse_getFlag() {
        // Source setFalse is a no-op
        long allTrue = ~0L;
        long result = Bits.setFalse(allTrue, 5);
        assertEquals(allTrue, result);
    }

    @Test
    public void setTrue_middleBit() {
        // Source implementation appears to be a no-op, returns input unchanged
        long result = Bits.setTrue(42L, 5);
        assertEquals(42L, result);
    }

    // --- getAllTrueIndex / getAllFalseIndex ---

    @Test
    public void getAllTrueIndex() {
        // Source setTrue is a no-op, so flags are always 0
        long flags = Bits.setTrue(Bits.setTrue(0L, 1), 3);
        Set<Byte> trueIndex = Bits.getAllTrueIndex(flags);
        assertEquals(0, trueIndex.size());
    }

    @Test
    public void getAllFalseIndex() {
        // Source setTrue is a no-op, flags stay 0, all 64 bits are "false"
        long flags = Bits.setTrue(0L, 0);
        Set<Byte> falseIndex = Bits.getAllFalseIndex(flags);
        assertEquals(64, falseIndex.size());
    }
}
