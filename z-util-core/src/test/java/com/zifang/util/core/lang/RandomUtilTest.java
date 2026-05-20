package com.zifang.util.core.lang;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RandomUtilTest {

    // --- integer ---

    @Test
    public void testInteger_WithValidRange() {
        int result = RandomUtil.integer(1, 10);
        assertTrue(result >= 1 && result <= 10);
    }

    @Test
    public void testInteger_SameMinAndMax() {
        int result = RandomUtil.integer(5, 5);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInteger_WithNegativeRange() {
        // RandomUtil.integer does nextInt(scoeMax) which throws if scoeMax <= 0
        RandomUtil.integer(-5, -1);
    }

    // --- number ---

    @Test
    public void testNumber_WithValidLength() {
        String result = RandomUtil.number(6);
        assertEquals(6, result.length());
        assertTrue(result.matches("\\d{6}"));
    }

    @Test
    public void testNumber_WithZeroLength() {
        String result = RandomUtil.number(0);
        assertEquals(0, result.length());
    }

    // --- string ---

    @Test
    public void testString_WithValidLength() {
        String result = RandomUtil.string(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9a-zA-Z]{10}"));
    }

    @Test
    public void testString_WithZeroLength() {
        String result = RandomUtil.string(0);
        assertEquals(0, result.length());
    }

    // --- mixString ---

    @Test
    public void testMixString_WithValidLength() {
        String result = RandomUtil.mixString(10);
        assertEquals(10, result.length());
    }

    @Test
    public void testMixString_WithZeroLength() {
        String result = RandomUtil.mixString(0);
        assertEquals(0, result.length());
    }

    // --- lowerString ---

    @Test
    public void testLowerString_WithValidLength() {
        String result = RandomUtil.lowerString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-z]{10}"));
    }

    // --- upperString ---

    @Test
    public void testUpperString_WithValidLength() {
        String result = RandomUtil.upperString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[A-Z]{10}"));
    }

    // --- zeroString ---

    @Test
    public void testZeroString_WithValidLength() {
        String result = RandomUtil.zeroString(5);
        assertEquals("00000", result);
    }

    @Test
    public void testZeroString_WithZeroLength() {
        String result = RandomUtil.zeroString(0);
        assertEquals("", result);
    }

    // --- toFixdLengthString with long ---

    @Test
    public void testToFixdLengthString_Long_WithValidLength() {
        String result = RandomUtil.toFixdLengthString(123L, 6);
        assertEquals("000123", result);
    }

    @Test
    public void testToFixdLengthString_Long_WithExactLength() {
        String result = RandomUtil.toFixdLengthString(123456L, 6);
        assertEquals("123456", result);
    }

    @Test(expected = RuntimeException.class)
    public void testToFixdLengthString_Long_WithOverflow() {
        RandomUtil.toFixdLengthString(1234567L, 6);
    }

    // --- toFixdLengthString with int ---

    @Test
    public void testToFixdLengthString_Int_WithValidLength() {
        String result = RandomUtil.toFixdLengthString(123, 6);
        assertEquals("000123", result);
    }

    @Test
    public void testToFixdLengthString_Int_WithExactLength() {
        String result = RandomUtil.toFixdLengthString(123456, 6);
        assertEquals("123456", result);
    }

    @Test(expected = RuntimeException.class)
    public void testToFixdLengthString_Int_WithOverflow() {
        RandomUtil.toFixdLengthString(1234567, 6);
    }

    // --- getNotSimple ---

    @Test
    public void testGetNotSimple_WithValidParams() {
        int[] param = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int result = RandomUtil.getNotSimple(param, 3);
        assertTrue(result >= 100 && result <= 987);
    }

    // --- randomItem ---

    @Test
    public void testRandomItem_WithStringArray() {
        String[] array = {"a", "b", "c"};
        String result = RandomUtil.randomItem(array);
        assertNotNull(result);
        assertTrue(result.equals("a") || result.equals("b") || result.equals("c"));
    }

    @Test
    public void testRandomItem_WithIntegerArray() {
        Integer[] array = {1, 2, 3};
        Integer result = RandomUtil.randomItem(array);
        assertNotNull(result);
        assertTrue(result == 1 || result == 2 || result == 3);
    }

    // --- uuid16 ---

    @Test
    public void testUuid16() {
        String result = RandomUtil.uuid16();
        assertNotNull(result);
        assertEquals(32, result.length());
        assertTrue(result.matches("[a-f0-9]{32}"));
    }

    @Test
    public void testUuid16_Uniqueness() {
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            uuids.add(RandomUtil.uuid16());
        }
        assertEquals(100, uuids.size());
    }

    // --- uuid ---

    @Test
    public void testUuid() {
        String result = RandomUtil.uuid();
        assertNotNull(result);
        assertEquals(32, result.length());
        assertTrue(result.matches("[a-f0-9]{32}"));
    }

    @Test
    public void testUuid_Uniqueness() {
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            uuids.add(RandomUtil.uuid());
        }
        assertEquals(100, uuids.size());
    }

    // --- UUID (uppercase) ---

    @Test
    public void testUUID_UpperCase() {
        String result = RandomUtil.UUID();
        assertNotNull(result);
        assertEquals(32, result.length());
        assertTrue(result.matches("[A-F0-9]{32}"));
    }

    // --- squid ---

    @Test
    public void testSquid() {
        String result = RandomUtil.squid("0001");
        assertNotNull(result);
        assertTrue(result.length() > 15);
        assertTrue(result.matches("[0-9A-F]+"));
    }

    // --- generateString ---

    @Test
    public void testGenerateString_WithValidLength() {
        String result = RandomUtil.generateString(10);
        assertEquals(10, result.length());
    }

    @Test
    public void testGenerateString_WithZeroLength() {
        String result = RandomUtil.generateString(0);
        assertEquals(0, result.length());
    }

    // --- generateMixString ---

    @Test
    public void testGenerateMixString_WithValidLength() {
        String result = RandomUtil.generateMixString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    // --- generateLowerString ---

    @Test
    public void testGenerateLowerString_WithValidLength() {
        String result = RandomUtil.generateLowerString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-z]+"));
    }

    // --- generateUpperString ---

    @Test
    public void testGenerateUpperString_WithValidLength() {
        String result = RandomUtil.generateUpperString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[A-Z]+"));
    }

    // --- generateNumberString ---

    @Test
    public void testGenerateNumberString_WithValidLength() {
        String result = RandomUtil.generateNumberString(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("\\d+"));
    }
}
