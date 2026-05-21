package com.zifang.util.core.io.file;

import com.zifang.util.core.io.FastByteBuffer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * FileHashUtil 测试
 * 覆盖：md5/sha1/sha256/hash/fileType/isImage
 */
public class FileHashUtilTest {

    private File tmpFile(String content) throws Exception {
        File f = File.createTempFile("hash-test", ".txt");
        f.deleteOnExit();
        Files.write(f.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return f;
    }

    private String md5Bytes(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    private String sha256Bytes(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    private String sha1Bytes(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // ==================== md5 ====================

    @Test
    public void testMd5File() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = md5Bytes("hello world");
        assertEquals(expected, FileHashUtil.md5(tmp));
    }

    @Test
    public void testMd5FileChinese() throws Exception {
        File tmp = tmpFile("中文测试");
        String expected = md5Bytes("中文测试");
        assertEquals(expected, FileHashUtil.md5(tmp));
    }

    @Test
    public void testMd5FileEmpty() throws Exception {
        File tmp = tmpFile("");
        String expected = md5Bytes("");
        assertEquals(expected, FileHashUtil.md5(tmp));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMd5Null() {
        FileHashUtil.md5(null);
    }

    @Test(expected = IOException.class)
    public void testMd5NonExistent() {
        FileHashUtil.md5(new File("/non/existent/file.txt"));
    }

    // ==================== sha1 ====================

    @Test
    public void testSha1File() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = sha1Bytes("hello world");
        assertEquals(expected, FileHashUtil.sha1(tmp));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSha1Null() {
        FileHashUtil.sha1(null);
    }

    @Test(expected = IOException.class)
    public void testSha1NonExistent() {
        FileHashUtil.sha1(new File("/non/existent/file.txt"));
    }

    // ==================== sha256 ====================

    @Test
    public void testSha256File() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = sha256Bytes("hello world");
        assertEquals(expected, FileHashUtil.sha256(tmp));
    }

    @Test
    public void testSha256FileChinese() throws Exception {
        File tmp = tmpFile("中文测试");
        String expected = sha256Bytes("中文测试");
        assertEquals(expected, FileHashUtil.sha256(tmp));
    }

    @Test
    public void testSha256FileEmpty() throws Exception {
        File tmp = tmpFile("");
        String expected = sha256Bytes("");
        assertEquals(expected, FileHashUtil.sha256(tmp));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSha256Null() {
        FileHashUtil.sha256(null);
    }

    @Test(expected = IOException.class)
    public void testSha256NonExistent() {
        FileHashUtil.sha256(new File("/non/existent/file.txt"));
    }

    // ==================== hash (generic) ====================

    @Test
    public void testHashFile() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = sha256Bytes("hello world");
        assertEquals(expected, FileHashUtil.hash(tmp, "SHA-256"));
    }

    @Test
    public void testHashFileMD5() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = md5Bytes("hello world");
        assertEquals(expected, FileHashUtil.hash(tmp, "MD5"));
    }

    @Test
    public void testHashFileSHA1() throws Exception {
        File tmp = tmpFile("hello world");
        String expected = sha1Bytes("hello world");
        assertEquals(expected, FileHashUtil.hash(tmp, "SHA-1"));
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void testHashInvalidAlgorithm() throws Exception {
        File tmp = tmpFile("test");
        FileHashUtil.hash(tmp, "INVALID_ALGO");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashAlgorithmNull() throws Exception {
        File tmp = tmpFile("test");
        FileHashUtil.hash(tmp, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashFileNull() throws Exception {
        FileHashUtil.hash(null, "MD5");
    }

    // ==================== fileType (magic bytes) ====================

    @Test
    public void testFileTypePng() throws Exception {
        // PNG magic bytes: 89 50 4E 47 0D 0A 1A 0A
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52
        };
        File tmp = File.createTempFile("magic", ".png");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), pngData);
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
        File tmp = File.createTempFile("magic", ".jpg");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), jpgData);
        assertEquals("jpg", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeGif() throws Exception {
        // GIF magic bytes: 47 49 46 38 (GIF8)
        byte[] gifData = new byte[]{
                0x47, 0x49, 0x46, 0x38, 0x39, 0x61,
                0x01, 0x00, 0x01, 0x00, 0x80, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic", ".gif");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), gifData);
        assertEquals("gif", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypePdf() throws Exception {
        // PDF magic bytes: 25 50 44 46 (%PDF)
        byte[] pdfData = new byte[]{
                0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x34,
                0x0A, 0x25, (byte) 0xC7, (byte) 0xEC, 0x0A
        };
        File tmp = File.createTempFile("magic", ".pdf");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), pdfData);
        assertEquals("pdf", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeZip() throws Exception {
        // ZIP magic bytes: 50 4B 03 04
        byte[] zipData = new byte[]{
                0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic", ".zip");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), zipData);
        assertEquals("zip", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeHtml() throws Exception {
        byte[] htmlData = "<!DOCTYPE html>".getBytes(StandardCharsets.UTF_8);
        File tmp = File.createTempFile("magic", ".html");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), htmlData);
        assertEquals("html", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeBmp() throws Exception {
        // BMP magic bytes: 42 4D (BM)
        byte[] bmpData = new byte[]{
                0x42, 0x4D, 0x3C, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x36, 0x00, 0x00, 0x00
        };
        File tmp = File.createTempFile("magic", ".bmp");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), bmpData);
        assertEquals("bmp", FileHashUtil.fileType(tmp));
        tmp.delete();
    }

    @Test
    public void testFileTypeUnknown() throws Exception {
        File tmp = tmpFile("random content with no magic bytes");
        assertNull(FileHashUtil.fileType(tmp));
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
    public void testIsImagePng() throws Exception {
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        };
        File tmp = File.createTempFile("image", ".png");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), pngData);
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageJpg() throws Exception {
        byte[] jpgData = new byte[]{
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0
        };
        File tmp = File.createTempFile("image", ".jpg");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), jpgData);
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageGif() throws Exception {
        byte[] gifData = new byte[]{
                0x47, 0x49, 0x46, 0x38, 0x39, 0x61
        };
        File tmp = File.createTempFile("image", ".gif");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), gifData);
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageNotImage() throws Exception {
        File tmp = tmpFile("not an image");
        assertFalse(FileHashUtil.isImage(tmp));
    }

    @Test
    public void testIsImageBmp() throws Exception {
        byte[] bmpData = new byte[]{0x42, 0x4D};
        File tmp = File.createTempFile("image", ".bmp");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), bmpData);
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test
    public void testIsImageTiff() throws Exception {
        byte[] tiffData = new byte[]{
                (byte) 0x49, 0x49, 0x2A, 0x00
        };
        File tmp = File.createTempFile("image", ".tif");
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), tiffData);
        assertTrue(FileHashUtil.isImage(tmp));
        tmp.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsImageNull() throws Exception {
        FileHashUtil.isImage(null);
    }

    @Test(expected = IOException.class)
    public void testIsImageNonExistent() {
        FileHashUtil.isImage(new File("/non/existent/file.txt"));
    }
}
