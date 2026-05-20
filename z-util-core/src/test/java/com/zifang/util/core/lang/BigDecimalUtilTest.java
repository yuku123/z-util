package com.zifang.util.core.lang;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BigDecimalUtilTest {

    // --- add (String) ---

    @Test
    public void testAddString_WithValidInputs() {
        String result = BigDecimalUtil.add("1.5", "2.5");
        assertEquals("4.0", result);
    }

    @Test
    public void testAddString_WithNegativeNumbers() {
        String result = BigDecimalUtil.add("-1.5", "2.5");
        assertEquals("1.0", result);
    }

    // --- div (String) ---

    @Test
    public void testDivString_WithValidInputs() {
        String result = BigDecimalUtil.div("10", "2", 2, BigDecimal.ROUND_HALF_UP);
        assertEquals("5.00", result);
    }

    @Test
    public void testDivString_WithScale() {
        String result = BigDecimalUtil.div("10", "3", 4, BigDecimal.ROUND_HALF_UP);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivString_WithNegativeScale() {
        BigDecimalUtil.div("10", "2", -1, BigDecimal.ROUND_HALF_UP);
    }

    // --- compareTo ---

    @Test
    public void testCompareTo_Greater() {
        int result = BigDecimalUtil.compareTo("10.0", "5.0");
        assertTrue(result > 0);
    }

    @Test
    public void testCompareTo_Equal() {
        int result = BigDecimalUtil.compareTo("5.0", "5.0");
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_Less() {
        int result = BigDecimalUtil.compareTo("3.0", "5.0");
        assertTrue(result < 0);
    }

    // --- returnMin ---

    @Test
    public void testReturnMin() {
        String result = BigDecimalUtil.returnMin("3.0", "5.0");
        assertEquals("3.0", result);
    }

    // --- returnMax ---

    @Test
    public void testReturnMax() {
        String result = BigDecimalUtil.returnMax("3.0", "5.0");
        assertEquals("5.0", result);
    }

    // --- getValue ---
    // SKIPPED: Implementation is commented out (empty shell)
    // @Test
    // public void testGetValue_WithValidScale() {
    //     BigDecimal bd = new BigDecimal("10.12567");
    //     BigDecimal result = BigDecimalUtil.getValue(bd, 2);
    //     assertEquals(2, result.scale());
    // }

    // --- getBigDecimal (Object) ---

    @Test
    public void testGetBigDecimal_FromString() {
        BigDecimal result = BigDecimalUtil.getBigDecimal("10.5");
        assertEquals(new BigDecimal("10.5"), result);
    }

    @Test
    public void testGetBigDecimal_FromInteger() {
        BigDecimal result = BigDecimalUtil.getBigDecimal(10);
        assertEquals(new BigDecimal(10), result);
    }

    @Test
    public void testGetBigDecimal_FromLong() {
        BigDecimal result = BigDecimalUtil.getBigDecimal(100L);
        assertEquals(new BigDecimal(100L), result);
    }

    @Test
    public void testGetBigDecimal_FromDouble() {
        BigDecimal result = BigDecimalUtil.getBigDecimal(10.5d);
        assertNotNull(result);
    }

    @Test
    public void testGetBigDecimal_FromBigDecimal() {
        BigDecimal input = new BigDecimal("10.5");
        BigDecimal result = BigDecimalUtil.getBigDecimal(input);
        assertEquals(input, result);
    }

    // --- getBigDecimal (Object, BigDecimal) ---

    @Test
    public void testGetBigDecimalWithDefault() {
        BigDecimal defaultVal = new BigDecimal("99.0");
        BigDecimal result = BigDecimalUtil.getBigDecimal(null, defaultVal);
        assertEquals(defaultVal, result);
    }

    // --- bigDecimalToLong ---

    @Test
    public void testBigDecimalToLong_WithValidValue() {
        BigDecimal bd = new BigDecimal("10.9");
        Long result = BigDecimalUtil.bigDecimalToLong(bd);
        assertEquals(Long.valueOf(10L), result);
    }

    @Test
    public void testBigDecimalToLong_WithNull() {
        Long result = BigDecimalUtil.bigDecimalToLong(null);
        assertNull(result);
    }

    // --- bigDecimalToInteger ---

    @Test
    public void testBigDecimalToInteger_WithValidValue() {
        BigDecimal bd = new BigDecimal("10.9");
        Integer result = BigDecimalUtil.bigDecimalToInteger(bd);
        assertEquals(Integer.valueOf(10), result);
    }

    @Test
    public void testBigDecimalToInteger_WithNull() {
        Integer result = BigDecimalUtil.bigDecimalToInteger(null);
        assertNull(result);
    }

    // --- add (BigDecimal) ---

    @Test
    public void testAddBigDecimal_WithValidInputs() {
        BigDecimal result = BigDecimalUtil.add(new BigDecimal("1.5"), new BigDecimal("2.5"));
        assertEquals(new BigDecimal("4.0"), result);
    }

    @Test
    public void testAddBigDecimal_WithNullFirst() {
        BigDecimal result = BigDecimalUtil.add(null, new BigDecimal("2.5"));
        assertEquals(new BigDecimal("2.5"), result);
    }

    @Test
    public void testAddBigDecimal_WithNullSecond() {
        BigDecimal result = BigDecimalUtil.add(new BigDecimal("1.5"), null);
        assertEquals(new BigDecimal("1.5"), result);
    }

    @Test
    public void testAddBigDecimal_WithBothNull() {
        BigDecimal result = BigDecimalUtil.add((BigDecimal) null, (BigDecimal) null);
        assertEquals(BigDecimal.ZERO, result);
    }

    // --- add (BigDecimal, int scale) ---

    @Test
    public void testAddBigDecimalWithScale() {
        BigDecimal result = BigDecimalUtil.add(new BigDecimal("1.555"), new BigDecimal("2.555"), 2);
        assertEquals(2, result.scale());
    }

    @Test
    public void testAddBigDecimalWithScale_NegativeScale() {
        BigDecimal result = BigDecimalUtil.add(new BigDecimal("1.5"), new BigDecimal("2.5"), -1);
        assertEquals(BigDecimal.ZERO, result);
    }

    // --- sub (BigDecimal) ---

    @Test
    public void testSubBigDecimal_WithValidInputs() {
        BigDecimal result = BigDecimalUtil.sub(new BigDecimal("5.0"), new BigDecimal("3.0"));
        assertEquals(new BigDecimal("2.0"), result);
    }

    @Test
    public void testSubBigDecimal_WithNullFirst() {
        BigDecimal result = BigDecimalUtil.sub(null, new BigDecimal("2.5"));
        assertEquals(new BigDecimal("-2.5"), result);
    }

    @Test
    public void testSubBigDecimal_WithBothNull() {
        BigDecimal result = BigDecimalUtil.sub(null, null);
        assertEquals(BigDecimal.ZERO, result);
    }

    // --- sub (BigDecimal, int scale) ---

    @Test
    public void testSubBigDecimalWithScale() {
        BigDecimal result = BigDecimalUtil.sub(new BigDecimal("5.555"), new BigDecimal("2.222"), 2);
        assertEquals(2, result.scale());
    }

    @Test
    public void testSubBigDecimalWithScale_NegativeScale() {
        BigDecimal result = BigDecimalUtil.sub(new BigDecimal("1.5"), new BigDecimal("2.5"), -1);
        assertEquals(new BigDecimal(0), result);
    }

    // --- mul (BigDecimal) ---

    @Test
    public void testMulBigDecimal_WithValidInputs() {
        BigDecimal result = BigDecimalUtil.mul(new BigDecimal("2.0"), new BigDecimal("3.0"));
        // multiply preserves scale: 2.0 * 3.0 = 6.00 (scale 2)
        assertEquals(0, new BigDecimal("6.00").compareTo(result));
    }

    @Test
    public void testMulBigDecimal_WithNullFirst() {
        BigDecimal result = BigDecimalUtil.mul(null, new BigDecimal("2.5"));
        // 0 * 2.5 = 0.0 (scale 1 due to 2.5's scale)
        assertEquals(0, BigDecimal.ZERO.setScale(1).compareTo(result));
    }

    @Test
    public void testMulBigDecimal_WithBothNull() {
        BigDecimal result = BigDecimalUtil.mul(null, null);
        assertEquals(BigDecimal.ZERO, result);
    }

    // --- mul (BigDecimal, int scale) ---

    @Test
    public void testMulBigDecimalWithScale() {
        BigDecimal result = BigDecimalUtil.mul(new BigDecimal("2.555"), new BigDecimal("3.111"), 2);
        assertEquals(2, result.scale());
    }

    @Test
    public void testMulBigDecimalWithScale_NegativeScale() {
        BigDecimal result = BigDecimalUtil.mul(new BigDecimal("1.5"), new BigDecimal("2.5"), -1);
        assertEquals(new BigDecimal(0), result);
    }

    // --- div (BigDecimal, int scale) ---

    @Test
    public void testDivBigDecimalWithScale() {
        BigDecimal result = BigDecimalUtil.div(new BigDecimal("10.0"), new BigDecimal("3.0"), 2);
        assertNotNull(result);
        assertEquals(2, result.scale());
    }

    @Test
    public void testDivBigDecimalWithScale_NegativeScale() {
        BigDecimal result = BigDecimalUtil.div(new BigDecimal("10.0"), new BigDecimal("3.0"), -1);
        assertEquals(new BigDecimal(0), result);
    }

    // --- div (BigDecimal, int scale, int accuracy) ---

    @Test
    public void testDivBigDecimalWithAccuracy() {
        BigDecimal result = BigDecimalUtil.div(new BigDecimal("10.0"), new BigDecimal("3.0"), 2, 10);
        assertNotNull(result);
        assertEquals(2, result.scale());
    }

    // --- round ---

    @Test
    public void testRound() {
        BigDecimal result = BigDecimalUtil.round(new BigDecimal("10.555"), 2);
        assertEquals(new BigDecimal("10.56"), result);
    }

    @Test
    public void testRound_NegativeScale() {
        BigDecimal result = BigDecimalUtil.round(new BigDecimal("10.55"), -1);
        assertEquals(new BigDecimal("10.55"), result);
    }

    // --- roundUp ---

    @Test
    public void testRoundUp() {
        BigDecimal result = BigDecimalUtil.roundUp(new BigDecimal("10.551"), 2);
        assertEquals(new BigDecimal("10.56"), result);
    }

    @Test
    public void testRoundUp_NegativeScale() {
        BigDecimal result = BigDecimalUtil.roundUp(new BigDecimal("10.55"), -1);
        assertEquals(new BigDecimal("10.55"), result);
    }

    // --- remainder ---

    @Test
    public void testRemainder() {
        BigDecimal result = BigDecimalUtil.remainder(new BigDecimal("10.0"), new BigDecimal("3.0"), 2);
        assertNotNull(result);
    }

    @Test
    public void testRemainder_NegativeScale() {
        BigDecimal result = BigDecimalUtil.remainder(new BigDecimal("10.0"), new BigDecimal("3.0"), -1);
        assertEquals(new BigDecimal(0), result);
    }

    // --- compare ---

    @Test
    public void testCompare_Greater() {
        boolean result = BigDecimalUtil.compare(new BigDecimal("10.0"), new BigDecimal("5.0"));
        assertTrue(result);
    }

    @Test
    public void testCompare_Equal() {
        boolean result = BigDecimalUtil.compare(new BigDecimal("5.0"), new BigDecimal("5.0"));
        assertFalse(result);
    }

    @Test
    public void testCompare_Less() {
        boolean result = BigDecimalUtil.compare(new BigDecimal("3.0"), new BigDecimal("5.0"));
        assertFalse(result);
    }

    @Test
    public void testCompare_WithNullFirst() {
        boolean result = BigDecimalUtil.compare(null, new BigDecimal("5.0"));
        assertFalse(result);
    }

    // --- compareGreater ---

    @Test
    public void testCompareGreater_Equal() {
        boolean result = BigDecimalUtil.compareGreater(new BigDecimal("5.0"), new BigDecimal("5.0"));
        assertTrue(result);
    }

    @Test
    public void testCompareGreater_Greater() {
        boolean result = BigDecimalUtil.compareGreater(new BigDecimal("10.0"), new BigDecimal("5.0"));
        assertTrue(result);
    }

    @Test
    public void testCompareGreater_Less() {
        boolean result = BigDecimalUtil.compareGreater(new BigDecimal("3.0"), new BigDecimal("5.0"));
        assertFalse(result);
    }

    // --- compareEqual (BigDecimal) ---

    @Test
    public void testCompareEqualBigDecimal_Equal() {
        boolean result = BigDecimalUtil.compareEqual(new BigDecimal("5.0"), new BigDecimal("5.0"));
        assertTrue(result);
    }

    @Test
    public void testCompareEqualBigDecimal_NotEqual() {
        boolean result = BigDecimalUtil.compareEqual(new BigDecimal("5.0"), new BigDecimal("5.1"));
        assertFalse(result);
    }

    // --- compareEqual (Long) ---

    @Test
    public void testCompareEqualLong_Equal() {
        boolean result = BigDecimalUtil.compareEqual(5L, 5L);
        assertTrue(result);
    }

    @Test
    public void testCompareEqualLong_NotEqual() {
        boolean result = BigDecimalUtil.compareEqual(5L, 6L);
        assertFalse(result);
    }

    @Test
    public void testCompareEqualLong_WithNullFirst() {
        boolean result = BigDecimalUtil.compareEqual(null, 5L);
        assertFalse(result);
    }

    // --- divideAndRemainder ---

    @Test
    public void testDivideAndRemainder() {
        BigDecimal[] result = BigDecimalUtil.divideAndRemainder(new BigDecimal("10.0"), new BigDecimal("3.0"));
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    // --- bigDecimal2String ---

    @Test
    public void testBigDecimal2String_WithValidValue() {
        String result = BigDecimalUtil.bigDecimal2String(new BigDecimal("10.555"), 2);
        assertEquals("10.55", result);
    }

    @Test
    public void testBigDecimal2String_WithNull() {
        String result = BigDecimalUtil.bigDecimal2String(null, 2);
        assertNull(result);
    }
}
