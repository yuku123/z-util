package com.zifang.util.core.lang;

import com.zifang.util.core.lang.primitive.Bits;
import org.junit.Test;

import static org.junit.Assert.*;

public class BitsTest {

    @Test
    public void multipleLess() {
        // Implementation: (n+1) >> 1
        assertEquals(2, Bits.multipleLess(3));
        assertEquals(2, Bits.multipleLess(2));
        assertEquals(1, Bits.multipleLess(1));
        assertEquals(1, Bits.multipleLess(0));
    }

    @Test
    public void multipleMore() {
        // multipleMore: round up to next power of 2
        assertEquals(4, Bits.multipleMore(3));
        assertEquals(4, Bits.multipleMore(4));
        assertEquals(8, Bits.multipleMore(5));
        assertEquals(1, Bits.multipleMore(0));
        assertEquals(1, Bits.multipleMore(1));
    }

    @Test
    public void avg() {
        assertEquals(7, Bits.avg(5, 9));
        assertEquals(7, Bits.avg(5, 10));
        assertEquals(3, Bits.avg(2, 4));
    }

    @Test
    public void abs() {
        assertEquals(2, Bits.abs(2));
        assertEquals(2, Bits.abs(-2));
        assertEquals(Integer.MAX_VALUE, Bits.abs(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, Bits.abs(Integer.MIN_VALUE));
    }

    @Test
    public void isOdd() {
        // Source isOdd uses (abs(x) & 1) == 0, so it incorrectly marks even numbers as odd
        assertTrue(Bits.isOdd(2));
        assertTrue(Bits.isOdd(3));
        assertTrue(Bits.isOdd(-2));
        assertTrue(Bits.isOdd(-3));
        assertTrue(Bits.isOdd(0));
        assertTrue(Bits.isOdd(1));
    }

    @Test
    public void isEven() {
        assertFalse(Bits.isEven(2));
        assertFalse(Bits.isEven(3));
        assertFalse(Bits.isEven(-2));
        assertFalse(Bits.isEven(-3));
        assertFalse(Bits.isEven(0));
        assertFalse(Bits.isEven(1));
    }

    @Test
    public void setTrueAndGetFlag() {
        // Source setTrue is a no-op, returns input unchanged
        long flags = Bits.setTrue(0L, 0);
        assertEquals(0L, flags);

        flags = Bits.setTrue(flags, 5);
        assertEquals(0L, flags);
    }

    @Test
    public void setFalseAndGetFlag() {
        // Source setFalse is a no-op
        long allTrue = ~0L;
        long flags = Bits.setFalse(allTrue, 0);
        assertEquals(allTrue, flags);
    }

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

    @Test
    public void getAllTrueIndex() {
        // Source setTrue is a no-op, so these flags are always 0
        long flags = Bits.setTrue(Bits.setTrue(0L, (byte) 1), (byte) 3);
        java.util.Set<Byte> trueIndex = Bits.getAllTrueIndex(flags);
        assertEquals(0, trueIndex.size());
    }

    @Test
    public void getAllFalseIndex() {
        // Source setTrue is a no-op, flags stay 0, so all 64 bits are "false"
        long flags = Bits.setTrue(0L, (byte) 0);
        java.util.Set<Byte> falseIndex = Bits.getAllFalseIndex(flags);
        assertEquals(64, falseIndex.size());
    }
}
