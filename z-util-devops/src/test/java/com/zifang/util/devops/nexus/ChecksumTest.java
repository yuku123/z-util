package com.zifang.util.devops.nexus;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Checksum 类测试
 */
public class ChecksumTest {

    @Test
    public void testDefaultConstructor() {
        Checksum checksum = new Checksum();
        assertNotNull(checksum);
        assertNull(checksum.getSha1());
        assertNull(checksum.getMd5());
    }

    @Test
    public void testSha1SetterAndGetter() {
        Checksum checksum = new Checksum();
        checksum.setSha1("da39a3ee5e6b4b0d3255bfef95601890afd80709");
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", checksum.getSha1());
    }

    @Test
    public void testMd5SetterAndGetter() {
        Checksum checksum = new Checksum();
        checksum.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", checksum.getMd5());
    }

    @Test
    public void testEquals() {
        Checksum checksum1 = new Checksum();
        checksum1.setSha1("sha1hash");
        checksum1.setMd5("md5hash");

        Checksum checksum2 = new Checksum();
        checksum2.setSha1("sha1hash");
        checksum2.setMd5("md5hash");

        assertEquals(checksum1, checksum2);
    }

    @Test
    public void testEqualsWithDifferentSha1() {
        Checksum checksum1 = new Checksum();
        checksum1.setSha1("sha1hash1");

        Checksum checksum2 = new Checksum();
        checksum2.setSha1("sha1hash2");

        assertNotEquals(checksum1, checksum2);
    }

    @Test
    public void testEqualsWithDifferentMd5() {
        Checksum checksum1 = new Checksum();
        checksum1.setMd5("md5hash1");

        Checksum checksum2 = new Checksum();
        checksum2.setMd5("md5hash2");

        assertNotEquals(checksum1, checksum2);
    }

    @Test
    public void testEqualsWithSameObject() {
        Checksum checksum = new Checksum();
        assertEquals(checksum, checksum);
    }

    @Test
    public void testEqualsWithNull() {
        Checksum checksum = new Checksum();
        assertNotEquals(checksum, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Checksum checksum = new Checksum();
        assertNotEquals(checksum, "not a checksum");
    }

    @Test
    public void testHashCode() {
        Checksum checksum1 = new Checksum();
        checksum1.setSha1("sha1hash");
        checksum1.setMd5("md5hash");

        Checksum checksum2 = new Checksum();
        checksum2.setSha1("sha1hash");
        checksum2.setMd5("md5hash");

        assertEquals(checksum1.hashCode(), checksum2.hashCode());
    }

    @Test
    public void testHashCodeConsistency() {
        Checksum checksum = new Checksum();
        checksum.setSha1("sha1hash");

        int hashCode1 = checksum.hashCode();
        int hashCode2 = checksum.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Checksum checksum = new Checksum();
        checksum.setSha1("abc123");
        checksum.setMd5("def456");

        String str = checksum.toString();

        assertNotNull(str);
        assertTrue(str.contains("Checksum"));
        assertTrue(str.contains("abc123"));
        assertTrue(str.contains("def456"));
    }

    @Test
    public void testToStringWithNullValues() {
        Checksum checksum = new Checksum();
        String str = checksum.toString();

        assertNotNull(str);
        assertTrue(str.contains("Checksum"));
    }

    @Test
    public void testCompleteChecksum() {
        Checksum checksum = new Checksum();
        checksum.setSha1("da39a3ee5e6b4b0d3255bfef95601890afd80709");
        checksum.setMd5("d41d8cd98f00b204e9800998ecf8427e");

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", checksum.getSha1());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", checksum.getMd5());
    }

    @Test
    public void testChecksumWithOnlySha1() {
        Checksum checksum = new Checksum();
        checksum.setSha1("abc123");

        assertEquals("abc123", checksum.getSha1());
        assertNull(checksum.getMd5());
    }

    @Test
    public void testChecksumWithOnlyMd5() {
        Checksum checksum = new Checksum();
        checksum.setMd5("def456");

        assertNull(checksum.getSha1());
        assertEquals("def456", checksum.getMd5());
    }
}
