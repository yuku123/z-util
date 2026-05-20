package com.zifang.util.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashUtilTest {

    // --- additiveHash ---

    @Test
    public void testAdditiveHash_WithValidString() {
        int result = HashUtil.additiveHash("test", 31);
        assertTrue(result >= 0);
    }

    @Test
    public void testAdditiveHash_WithEmptyString() {
        int result = HashUtil.additiveHash("", 31);
        assertEquals(0 % 31, result);
    }

    @Test
    public void testAdditiveHash_DiffersWithDifferentPrime() {
        int result1 = HashUtil.additiveHash("test", 31);
        int result2 = HashUtil.additiveHash("test", 37);
        assertTrue(result1 != result2);
    }

    // --- rotatingHash ---

    @Test
    public void testRotatingHash_WithValidString() {
        int result = HashUtil.rotatingHash("test", 31);
        assertTrue(result >= 0);
    }

    @Test
    public void testRotatingHash_WithEmptyString() {
        int result = HashUtil.rotatingHash("", 31);
        assertEquals(0 % 31, result);
    }

    // --- oneByOneHash ---

    @Test
    public void testOneByOneHash_WithValidString() {
        int result = HashUtil.oneByOneHash("test");
        assertTrue(result != 0);
    }

    @Test
    public void testOneByOneHash_WithEmptyString() {
        int result = HashUtil.oneByOneHash("");
        assertEquals(0, result);
    }

    @Test
    public void testOneByOneHash_Deterministic() {
        int result1 = HashUtil.oneByOneHash("test");
        int result2 = HashUtil.oneByOneHash("test");
        assertEquals(result1, result2);
    }

    // --- bernstein ---

    @Test
    public void testBernstein_WithValidString() {
        int result = HashUtil.bernstein("test");
        assertTrue(result != 0);
    }

    @Test
    public void testBernstein_WithEmptyString() {
        int result = HashUtil.bernstein("");
        assertEquals(0, result);
    }

    // --- universal ---

    @Test
    public void testUniversal_WithValidInput() {
        char[] key = {'t', 'e', 's', 't'};
        int mask = 0x7FFFFFFF;
        int[] tab = new int[32];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = i;
        }
        int result = HashUtil.universal(key, mask, tab);
        assertTrue(result >= 0);
    }

    // --- zobrist ---

    @Test
    public void testZobrist_WithValidInput() {
        char[] key = {'t', 'e', 's', 't'};
        int mask = 0x7FFFFFFF;
        int[][] tab = new int[4][256];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                tab[i][j] = j;
            }
        }
        int result = HashUtil.zobrist(key, mask, tab);
        assertTrue(result >= 0);
    }

    // --- FNVHash ---

    @Test
    public void testFNVHash_WithValidByteArray() {
        byte[] data = {'t', 'e', 's', 't'};
        int result = HashUtil.FNVHash(data);
        assertTrue(result != 0);
    }

    @Test
    public void testFNVHash_Deterministic() {
        byte[] data = {'t', 'e', 's', 't'};
        int result1 = HashUtil.FNVHash(data);
        int result2 = HashUtil.FNVHash(data);
        assertEquals(result1, result2);
    }

    // --- FNVHash1 with byte array ---

    @Test
    public void testFNVHash1_ByteArray_WithValidInput() {
        byte[] data = {'t', 'e', 's', 't'};
        int result = HashUtil.FNVHash1(data);
        assertTrue(result != 0);
    }

    // --- FNVHash1 with String ---

    @Test
    public void testFNVHash1_String_WithValidInput() {
        int result = HashUtil.FNVHash1("test");
        assertTrue(result != 0);
    }

    // --- intHash ---

    @Test
    public void testIntHash_WithValidInt() {
        int result = HashUtil.intHash(12345);
        assertTrue(result != 12345);
    }

    @Test
    public void testIntHash_Deterministic() {
        int result1 = HashUtil.intHash(12345);
        int result2 = HashUtil.intHash(12345);
        assertEquals(result1, result2);
    }

    // --- RSHash ---

    @Test
    public void testRSHash_WithValidString() {
        int result = HashUtil.RSHash("test");
        assertTrue(result >= 0);
    }

    @Test
    public void testRSHash_Deterministic() {
        int result1 = HashUtil.RSHash("test");
        int result2 = HashUtil.RSHash("test");
        assertEquals(result1, result2);
    }

    // --- JSHash ---

    @Test
    public void testJSHash_WithValidString() {
        int result = HashUtil.JSHash("test");
        assertTrue(result >= 0);
    }

    @Test
    public void testJSHash_Deterministic() {
        int result1 = HashUtil.JSHash("test");
        int result2 = HashUtil.JSHash("test");
        assertEquals(result1, result2);
    }

    // --- PJWHash ---

    @Test
    public void testPJWHash_WithValidString() {
        int result = HashUtil.PJWHash("test");
        assertTrue(result >= 0);
    }

    // --- ELFHash ---

    @Test
    public void testELFHash_WithValidString() {
        int result = HashUtil.ELFHash("test");
        assertTrue(result >= 0);
    }

    // --- BKDRHash ---

    @Test
    public void testBKDRHash_WithValidString() {
        int result = HashUtil.BKDRHash("test");
        assertTrue(result >= 0);
    }

    @Test
    public void testBKDRHash_Deterministic() {
        int result1 = HashUtil.BKDRHash("test");
        int result2 = HashUtil.BKDRHash("test");
        assertEquals(result1, result2);
    }

    // --- SDBMHash ---

    @Test
    public void testSDBMHash_WithValidString() {
        int result = HashUtil.SDBMHash("test");
        assertTrue(result >= 0);
    }

    // --- DJBHash ---

    @Test
    public void testDJBHash_WithValidString() {
        int result = HashUtil.DJBHash("test");
        assertTrue(result >= 0);
    }

    // --- DEKHash ---

    @Test
    public void testDEKHash_WithValidString() {
        int result = HashUtil.DEKHash("test");
        assertTrue(result != 0);
    }

    // --- APHash ---

    @Test
    public void testAPHash_WithValidString() {
        int result = HashUtil.APHash("test");
        assertTrue(result != 0);
    }

    // --- java ---

    @Test
    public void testJavaHash_WithValidString() {
        int result = HashUtil.java("test");
        assertTrue(result != 0);
    }

    @Test
    public void testJavaHash_EqualsStringHashCode() {
        int result = HashUtil.java("test");
        assertEquals("test".hashCode(), result);
    }

    // --- mixHash ---

    @Test
    public void testMixHash_WithValidString() {
        long result = HashUtil.mixHash("test");
        assertTrue(result != 0);
    }

    @Test
    public void testMixHash_Deterministic() {
        long result1 = HashUtil.mixHash("test");
        long result2 = HashUtil.mixHash("test");
        assertEquals(result1, result2);
    }

    // --- hash ---

    @Test
    public void testHash_WithNonNullObject() {
        int result = HashUtil.hash("test");
        assertTrue(result != 0);
    }

    @Test
    public void testHash_WithNullObject() {
        int result = HashUtil.hash(null);
        assertEquals(0, result);
    }

    @Test
    public void testHash_Deterministic() {
        int result1 = HashUtil.hash("test");
        int result2 = HashUtil.hash("test");
        assertEquals(result1, result2);
    }
}
