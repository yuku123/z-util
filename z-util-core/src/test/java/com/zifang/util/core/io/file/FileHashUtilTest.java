package com.zifang.util.core.io.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class FileHashUtilTest {

    private File tempBaseDir;

    @Before
    public void setUp() throws Exception {
        tempBaseDir = Files.createTempDirectory("hash-test-base").toFile();
        tempBaseDir.deleteOnExit();
    }

    @After
    public void tearDown() throws Exception {
        if (tempBaseDir != null && tempBaseDir.exists()) {
            deleteRecursively(tempBaseDir);
        }
    }

    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }

    private String md5String(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String sha256String(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // ==================== md5 ====================

    @Test
    public void testMd5String() throws Exception {
        String input = "hello world";
        String expected = md5String(input);
        assertEquals(expected, FileHashUtil.md5(input));
    }

    @Test
    public void testMd5StringChinese() throws Exception {
        String input = "中文测试";
        String expected = md5String(input);
        assertEquals(expected, FileHashUtil.md5(input));
    }

    @Test
    public void testMd5EmptyString() throws Exception {
        String input = "";
        String expected = md5String(input);
        assertEquals(expected, FileHashUtil.md5(input));
    }

    @Test
    public void testMd5File() throws Exception {
        File tmp = File.createTempFile("md5-test", ".txt");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), "hello world".getBytes(StandardCharsets.UTF_8));

        String expected = md5String("hello world");
        assertEquals(expected, FileHashUtil.md5(tmp));
        tmp.delete();
    }

    @Test
    public void testMd5FileEmpty() throws Exception {
        File tmp = File.createTempFile("md5-empty", ".txt");
        tmp.deleteOnExit();
        String expected = md5String("");
        assertEquals(expected, FileHashUtil.md5(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMd5StringNull() {
        FileHashUtil.md5((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMd5FileNull() {
        FileHashUtil.md5((File) null);
    }

    @Test(expected = IOException.class)
    public void testMd5FileNonExistent() {
        FileHashUtil.md5(new File("/non/existent/file.txt"));
    }

    // ==================== sha1 ====================

    @Test
    public void testSha1String() throws Exception {
        String input = "hello world";
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        assertEquals(sb.toString(), FileHashUtil.sha1(input));
    }

    @Test
    public void testSha1File() throws Exception {
        File tmp = File.createTempFile("sha1-test", ".txt");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), "hello world".getBytes(StandardCharsets.UTF_8));

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest("hello world".getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        assertEquals(sb.toString(), FileHashUtil.sha1(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSha1StringNull() {
        FileHashUtil.sha1((String) null);
    }

    @Test(expected = IOException.class)
    public void testSha1FileNonExistent() {
        FileHashUtil.sha1(new File("/non/existent/file.txt"));
    }

    // ==================== sha256 ====================

    @Test
    public void testSha256String() throws Exception {
        String input = "hello world";
        String expected = sha256String(input);
        assertEquals(expected, FileHashUtil.sha256(input));
    }

    @Test
    public void testSha256File() throws Exception {
        File tmp = File.createTempFile("sha256-test", ".txt");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), "hello world".getBytes(StandardCharsets.UTF_8));

        String expected = sha256String("hello world");
        assertEquals(expected, FileHashUtil.sha256(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSha256StringNull() {
        FileHashUtil.sha256((String) null);
    }

    @Test(expected = IOException.class)
    public void testSha256FileNonExistent() {
        FileHashUtil.sha256(new File("/non/existent/file.txt"));
    }

    // ==================== hash (generic) ====================

    @Test
    public void testHashString() throws Exception {
        String input = "hello world";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        assertEquals(sb.toString(), FileHashUtil.hash(input, "MD5"));
    }

    @Test
    public void testHashFile() throws Exception {
        File tmp = File.createTempFile("hash-test", ".txt");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), "hello world".getBytes(StandardCharsets.UTF_8));

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest("hello world".getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        assertEquals(sb.toString(), FileHashUtil.hash(tmp, "SHA-256"));
        tmp.delete();
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void testHashInvalidAlgorithm() {
        FileHashUtil.hash("test", "INVALID_ALGO");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashStringNull() {
        FileHashUtil.hash(null, "MD5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashAlgorithmNull() {
        FileHashUtil.hash("test", null);
    }

    // ==================== fileType (magic bytes) ====================

    @Test
    public void testFileTypePng() throws Exception {
        // PNG magic bytes: 89 50 4E 47 0D 0A 1A 0A
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52
        };
        File tmp = File.createTempFile("magic-test", ".png");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(pngData);
        }
        assertEquals("png", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeJpg() throws Exception {
        // JPEG magic bytes: FF D8 FF
        byte[] jpgData = new byte[]{
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0,
                0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01
        };
        File tmp = File.createTempFile("magic-test", ".jpg");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(jpgData);
        }
        assertEquals("jpg", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeGif() throws Exception {
        // GIF87a magic: 47 49 46 38 37 61
        byte[] gifData = new byte[]{
                0x47, 0x49, 0x46, 0x38, 0x37, 0x61, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic-test", ".gif");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(gifData);
        }
        assertEquals("gif", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeZip() throws Exception {
        // ZIP magic: 50 4B 03 04
        byte[] zipData = new byte[]{
                0x50, 0x4B, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic-test", ".zip");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(zipData);
        }
        assertEquals("zip", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypePdf() throws Exception {
        // PDF magic: 25 50 44 46 2D 31 2E
        byte[] pdfData = new byte[]{
                0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x00
        };
        File tmp = File.createTempFile("magic-test", ".pdf");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(pdfData);
        }
        assertEquals("pdf", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeHtml() throws Exception {
        // HTML: 68 74 6D 6C 3E (= "<html>")
        byte[] htmlData = new byte[]{
                0x68, 0x74, 0x6D, 0x6C, 0x3E, 0x0A, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic-test", ".html");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(htmlData);
        }
        assertEquals("html", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeBmp() throws Exception {
        // BMP magic: 42 4D (= "BM")
        byte[] bmpData = new byte[]{
                0x42, 0x4D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic-test", ".bmp");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(bmpData);
        }
        assertEquals("bmp", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeUnknown() throws Exception {
        // Random bytes not matching any known magic
        byte[] randomData = new byte[]{
                0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77
        };
        File tmp = File.createTempFile("magic-test", ".bin");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(randomData);
        }
        assertNull(FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeSmallFile() throws Exception {
        File tmp = File.createTempFile("tiny-test", ".bin");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), new byte[]{0x00, 0x01});
        assertNull(FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTypeNull() {
        FileHashUtil.fileType(null);
    }

    @Test(expected = IOException.class)
    public void testFileTypeNonExistent() {
        FileHashUtil.fileType(new File("/non/existent/file.txt"));
    }

    // ==================== isImage ====================

    @Test
    public void testIsImageTrueForPng() throws Exception {
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52
        };
        File tmp = File.createTempFile("image-test", ".png");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(pngData);
        }
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageTrueForJpg() throws Exception {
        byte[] jpgData = new byte[]{
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0,
                0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01
        };
        File tmp = File.createTempFile("image-test", ".jpg");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(jpgData);
        }
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageFalseForPdf() throws Exception {
        byte[] pdfData = new byte[]{
                0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x00
        };
        File tmp = File.createTempFile("image-test", ".pdf");
        tmp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(pdfData);
        }
        assertFalse(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageFalseForRandomFile() throws Exception {
        File tmp = File.createTempFile("image-test", ".bin");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), new byte[]{0x00, 0x11, 0x22, 0x33});
        assertFalse(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsImageNull() {
        FileHashUtil.isImage(null);
    }

    @Test(expected = IOException.class)
    public void testIsImageNonExistent() {
        FileHashUtil.isImage(new File("/non/existent/file.txt"));
    }
}
