package com.zifang.util.core.io.file;

import org.junit.Test;

import static org.junit.Assert.*;

public class FilePathUtilTest {

    // ==================== getName ====================

    @Test
    public void testGetNameWithUnixPath() {
        assertEquals("file.txt", FilePathUtil.getName("/home/user/file.txt"));
    }

    @Test
    public void testGetNameWithWindowsPath() {
        assertEquals("file.txt", FilePathUtil.getName("C:\\Users\\user\\file.txt"));
    }

    @Test
    public void testGetNameWithFileOnly() {
        assertEquals("file.txt", FilePathUtil.getName("file.txt"));
    }

    @Test
    public void testGetNameWithTrailingSeparator() {
        assertEquals("", FilePathUtil.getName("/home/user/"));
    }

    @Test
    public void testGetNameWithPathOnly() {
        assertEquals("user", FilePathUtil.getName("/home/user"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithNull() {
        FilePathUtil.getName(null);
    }

    // ==================== getBaseName ====================

    @Test
    public void testGetBaseNameWithExtension() {
        assertEquals("file", FilePathUtil.getBaseName("/home/user/file.txt"));
    }

    @Test
    public void testGetBaseNameWithoutExtension() {
        assertEquals("file", FilePathUtil.getBaseName("/home/user/file"));
    }

    @Test
    public void testGetBaseNameWithHiddenFile() {
        assertEquals(".bashrc", FilePathUtil.getBaseName("/home/user/.bashrc"));
    }

    @Test
    public void testGetBaseNameWithMultipleDots() {
        assertEquals("archive.tar", FilePathUtil.getBaseName("/home/user/archive.tar.gz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseNameWithNull() {
        FilePathUtil.getBaseName(null);
    }

    // ==================== getExtension ====================

    @Test
    public void testGetExtensionWithNormalFile() {
        assertEquals("txt", FilePathUtil.getExtension("/home/user/file.txt"));
    }

    @Test
    public void testGetExtensionWithNoExtension() {
        assertEquals("", FilePathUtil.getExtension("/home/user/file"));
    }

    @Test
    public void testGetExtensionWithHiddenFile() {
        assertEquals("bashrc", FilePathUtil.getExtension("/home/user/.bashrc"));
    }

    @Test
    public void testGetExtensionWithMultipleDots() {
        assertEquals("gz", FilePathUtil.getExtension("/home/user/archive.tar.gz"));
    }

    @Test
    public void testGetExtensionWithPathOnly() {
        assertEquals("", FilePathUtil.getExtension("/home/user/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExtensionWithNull() {
        FilePathUtil.getExtension(null);
    }

    // ==================== getParent ====================

    @Test
    public void testGetParentWithUnixPath() {
        assertEquals("/home/user", FilePathUtil.getParent("/home/user/file.txt"));
    }

    @Test
    public void testGetParentWithWindowsPath() {
        assertEquals("C:\\Users\\user", FilePathUtil.getParent("C:\\Users\\user\\file.txt"));
    }

    @Test
    public void testGetParentWithNoParent() {
        assertNull(FilePathUtil.getParent("file.txt"));
    }

    @Test
    public void testGetParentWithNull() {
        assertNull(FilePathUtil.getParent(null));
    }

    // ==================== normalize ====================

    @Test
    public void testNormalizeWithDots() {
        String result = FilePathUtil.normalize("/home/user/../user/file.txt");
        assertEquals("/home/user/file.txt", result);
    }

    @Test
    public void testNormalizeWithCurrentDir() {
        String result = FilePathUtil.normalize("/home/user/./file.txt");
        assertEquals("/home/user/file.txt", result);
    }

    @Test
    public void testNormalizeWithDoubleSlashes() {
        String result = FilePathUtil.normalize("/home//user//file.txt");
        assertEquals("/home/user/file.txt", result);
    }

    @Test
    public void testNormalizeWithWindowsBackslashes() {
        String result = FilePathUtil.normalize("C:\\Users\\..\\Users\\file.txt");
        assertEquals("C:\\Users\\file.txt", result);
    }

    @Test
    public void testNormalizeWithNull() {
        assertNull(FilePathUtil.normalize(null));
    }

    // ==================== toUNIXPath ====================

    @Test
    public void testToUNIXPathWithWindowsPath() {
        String result = FilePathUtil.toUNIXPath("C:\\Users\\user\\file.txt");
        assertEquals("C:/Users/user/file.txt", result);
    }

    @Test
    public void testToUNIXPathWithUnixPath() {
        String result = FilePathUtil.toUNIXPath("/home/user/file.txt");
        assertEquals("/home/user/file.txt", result);
    }

    @Test
    public void testToUNIXPathWithNull() {
        assertNull(FilePathUtil.toUNIXPath(null));
    }

    // ==================== getPrefix ====================

    @Test
    public void testGetPrefixWithUnixPath() {
        assertEquals("/", FilePathUtil.getPrefix("/home/user/file.txt"));
    }

    @Test
    public void testGetPrefixWithWindowsDriveLetter() {
        String prefix = FilePathUtil.getPrefix("C:\\Users\\user\\file.txt");
        assertEquals("C:\\", prefix);
    }

    @Test
    public void testGetPrefixWithUNCPath() {
        String prefix = FilePathUtil.getPrefix("\\\\server\\share\\file.txt");
        assertEquals("\\\\server\\share\\", prefix);
    }

    @Test
    public void testGetPrefixWithRelativePath() {
        assertEquals("", FilePathUtil.getPrefix("user/file.txt"));
    }

    @Test
    public void testGetPrefixWithNull() {
        assertNull(FilePathUtil.getPrefix(null));
    }

    // ==================== getSuffix ====================

    @Test
    public void testGetSuffixWithMultipleDots() {
        assertEquals("tar.gz", FilePathUtil.getSuffix("/home/user/archive.tar.gz"));
    }

    @Test
    public void testGetSuffixWithNoSuffix() {
        assertEquals("", FilePathUtil.getSuffix("/home/user/file"));
    }

    @Test
    public void testGetSuffixWithSingleDot() {
        assertEquals("", FilePathUtil.getSuffix("/home/user/file."));
    }

    @Test
    public void testGetSuffixWithNull() {
        assertNull(FilePathUtil.getSuffix(null));
    }

    // ==================== trimExtension ====================

    @Test
    public void testTrimExtension() {
        assertEquals("/home/user/file", FilePathUtil.trimExtension("/home/user/file.txt"));
    }

    @Test
    public void testTrimExtensionWithNoExtension() {
        assertEquals("/home/user/file", FilePathUtil.trimExtension("/home/user/file"));
    }

    @Test
    public void testTrimExtensionWithNull() {
        assertNull(FilePathUtil.trimExtension(null));
    }

    // ==================== getSubpath ====================

    @Test
    public void testGetSubpath() {
        String result = FilePathUtil.getSubpath("/home/user/file.txt", "/home");
        assertEquals("user/file.txt", result);
    }

    @Test
    public void testGetSubpathWithNoCommonPrefix() {
        String result = FilePathUtil.getSubpath("/home/user/file.txt", "/var");
        assertEquals("/home/user/file.txt", result);
    }

    @Test
    public void testGetSubpathWithNull() {
        assertNull(FilePathUtil.getSubpath(null, "/home"));
    }

    @Test
    public void testGetSubpathWithNullBaseDir() {
        assertNull(FilePathUtil.getSubpath("/home/user/file.txt", null));
    }

    // ==================== isExist / isDirectory / isFile ====================

    @Test
    public void testIsExistWithExistingFile() throws Exception {
        java.io.File tmp = java.io.File.createTempFile("pathtest", ".txt");
        try {
            assertTrue(FilePathUtil.isExist(tmp.getPath()));
        } finally {
            tmp.delete();
        }
    }

    @Test
    public void testIsExistWithNonExistingPath() {
        assertFalse(FilePathUtil.isExist("/non/existing/path/12345.txt"));
    }

    @Test
    public void testIsExistWithNull() {
        assertFalse(FilePathUtil.isExist(null));
    }

    @Test
    public void testIsDirectoryWithDir() throws Exception {
        java.io.File tmpDir = java.io.File.createTempFile("dirtest", "");
        tmpDir.delete();
        tmpDir.mkdirs();
        try {
            assertTrue(FilePathUtil.isDirectory(tmpDir.getPath()));
            assertFalse(FilePathUtil.isFile(tmpDir.getPath()));
        } finally {
            tmpDir.delete();
        }
    }

    @Test
    public void testIsFileWithFile() throws Exception {
        java.io.File tmp = java.io.File.createTempFile("filetest", ".txt");
        try {
            assertTrue(FilePathUtil.isFile(tmp.getPath()));
            assertFalse(FilePathUtil.isDirectory(tmp.getPath()));
        } finally {
            tmp.delete();
        }
    }

    @Test
    public void testIsDirectoryWithNull() {
        assertFalse(FilePathUtil.isDirectory(null));
    }

    @Test
    public void testIsFileWithNull() {
        assertFalse(FilePathUtil.isFile(null));
    }
}
