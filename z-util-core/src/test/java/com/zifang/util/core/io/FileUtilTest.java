package com.zifang.util.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class FileUtilTest {

    private File tempBaseDir;

    @Before
    public void setUp() throws Exception {
        tempBaseDir = Files.createTempDirectory("fileutil-test-base").toFile();
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

    // ==================== getFileName / getFilePath ====================

    @Test
    public void testGetFileName() {
        assertEquals("file.txt", FileUtil.getFileName("/home/user/file.txt"));
        assertEquals("file.txt", FileUtil.getFileName("C:\\Users\\user\\file.txt"));
    }

    @Test
    public void testGetFilePath() {
        assertEquals("/home/user", FileUtil.getFilePath("/home/user/file.txt"));
    }

    // ==================== toUNIXpath ====================

    @Test
    public void testToUNIXpath() {
        assertEquals("C:/Users/user/file.txt", FileUtil.toUNIXpath("C:\\Users\\user\\file.txt"));
    }

    // ==================== getTypePart / getNamePart / trimType ====================

    @Test
    public void testGetTypePart() {
        assertEquals("txt", FileUtil.getTypePart("file.txt"));
    }

    @Test
    public void testGetNamePart() {
        assertEquals("file", FileUtil.getNamePart("file.txt"));
    }

    @Test
    public void testTrimType() {
        assertEquals("file", FileUtil.trimType("file.txt"));
    }

    // ==================== getSubpath ====================

    @Test
    public void testGetSubpath() {
        assertEquals("user/file.txt", FileUtil.getSubpath("/home/user/file.txt", "/home"));
    }

    // ==================== getPathPart ====================

    @Test
    public void testGetPathPart() {
        assertEquals("/home/user", FileUtil.getPathPart("/home/user/file.txt"));
    }

    // ==================== pathValidate ====================

    @Test
    public void testPathValidate() throws IOException {
        File f = File.createTempFile("validate", ".txt", tempBaseDir);
        assertTrue(FileUtil.pathValidate(f.getPath()));
        f.delete();
        assertFalse(FileUtil.pathValidate("/non/existent/path/12345.txt"));
    }

    // ==================== getFileContent ====================

    @Test
    public void testGetFileContent() throws Exception {
        File f = new File(tempBaseDir, "content.txt");
        Files.write(f.toPath(), "hello content".getBytes(StandardCharsets.UTF_8));
        assertEquals("hello content", FileUtil.getFileContent(f));
    }

    @Test
    public void testGetFileContentByPath() throws Exception {
        File f = new File(tempBaseDir, "content2.txt");
        Files.write(f.toPath(), "path content".getBytes(StandardCharsets.UTF_8));
        assertEquals("path content", FileUtil.getFileContent(f.getPath()));
    }

    // ==================== readString from URL ====================

    @Test
    public void testReadStringFromURL() throws Exception {
        File f = new File(tempBaseDir, "urltest.txt");
        Files.write(f.toPath(), "url test".getBytes(StandardCharsets.UTF_8));
        URL url = f.toURI().toURL();
        assertEquals("url test", FileUtil.readString(url, "UTF-8"));
    }

    // ==================== getFileContentFromCharset / saveFile2Charset ====================

    @Test
    public void testGetFileContentFromCharset() throws Exception {
        File f = new File(tempBaseDir, "charset.txt");
        Files.write(f.toPath(), "charset test".getBytes(StandardCharsets.UTF_8));
        assertEquals("charset test", FileUtil.getFileContentFromCharset(f, "UTF-8"));
    }

    @Test
    public void testSaveFile2Charset() throws Exception {
        File f = new File(tempBaseDir, "save-charset.txt");
        FileUtil.saveFile2Charset(f, "UTF-8", "saved content");
        assertEquals("saved content", FileUtil.getFileContent(f));
    }

    // ==================== mkDir / mkdir / mkdirs ====================

    @Test
    public void testMkDir() throws Exception {
        File dir = new File(tempBaseDir, "newdir");
        FileUtil.mkDir(dir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }

    @Test(expected = FileNotFoundException.class)
    public void testMkDirNull() throws Exception {
        FileUtil.mkDir(null);
    }

    @Test
    public void testMkdirFromString() throws Exception {
        File dir = new File(tempBaseDir, "mkdir-str");
        FileUtil.mkdir(dir.getPath());
        assertTrue(dir.exists());
    }

    @Test
    public void testMkdirs() throws Exception {
        File nested = new File(tempBaseDir, "a/b/c/d");
        FileUtil.mkdirs(nested.getPath());
        assertTrue(nested.exists());
    }

    // ==================== makeDirectory ====================

    @Test
    public void testMakeDirectory() throws Exception {
        File dir = new File(tempBaseDir, "makedir");
        boolean result = FileUtil.makeDirectory(dir);
        assertTrue(result);
        assertTrue(dir.exists());
    }

    @Test
    public void testMakeDirectoryFalseForFile() throws Exception {
        File f = File.createTempFile("notadir", ".txt", tempBaseDir);
        boolean result = FileUtil.makeDirectory(f);
        assertFalse(result);
    }

    // ==================== touch ====================

    @Test
    public void testTouchCreatesNewFile() throws Exception {
        File f = new File(tempBaseDir, "touched.txt");
        FileUtil.touch(f);
        assertTrue(f.exists());
    }

    @Test
    public void testTouchUpdatesExistingFile() throws Exception {
        File f = File.createTempFile("touch-existing", ".txt", tempBaseDir);
        long original = f.lastModified();
        Thread.sleep(10);
        FileUtil.touch(f);
        assertTrue(f.lastModified() >= original);
    }

    @Test
    public void testTouchString() throws Exception {
        File f = new File(tempBaseDir, "touch-str.txt");
        FileUtil.touch(f.getPath());
        assertTrue(f.exists());
    }

    // ==================== emptyDirectory ====================

    @Test
    public void testEmptyDirectory() throws Exception {
        File dir = new File(tempBaseDir, "toempty");
        dir.mkdirs();
        new File(dir, "file1.txt").createNewFile();
        new File(dir, "file2.txt").createNewFile();

        FileUtil.emptyDirectory(dir);
        assertTrue(dir.exists());
        assertEquals(0, dir.listFiles().length);
    }

    // ==================== deleteDirectory ====================

    @Test
    public void testDeleteDirectory() throws Exception {
        File dir = new File(tempBaseDir, "todelete");
        dir.mkdirs();
        new File(dir, "file.txt").createNewFile();
        File subDir = new File(dir, "subdir");
        subDir.mkdirs();
        new File(subDir, "nested.txt").createNewFile();

        FileUtil.deleteDirectory(dir);
        assertFalse(dir.exists());
    }

    @Test
    public void testDeleteDir() {
        File dir = new File(tempBaseDir, "deletedir");
        dir.mkdirs();
        boolean result = FileUtil.deleteDir(dir);
        assertTrue(result);
        assertFalse(dir.exists());
    }

    @Test
    public void testDeleteDirReturnsFalseForNonExistent() {
        File nonExistent = new File(tempBaseDir, "non-existent-dir");
        assertFalse(FileUtil.deleteDir(nonExistent));
    }

    // ==================== dirRename ====================

    @Test
    public void testDirRename() throws Exception {
        File dir = new File(tempBaseDir, "oldname");
        dir.mkdirs();
        new File(dir, "file.txt").createNewFile();

        File newDir = new File(tempBaseDir, "newname");
        boolean result = FileUtil.dirRename(dir.getPath(), newDir.getPath());
        assertTrue(result);
        assertFalse(dir.exists());
        assertTrue(newDir.exists());
        assertTrue(new File(newDir, "file.txt").exists());
    }

    @Test
    public void testDirRenameFile() throws Exception {
        File file = File.createTempFile("oldfile", ".txt", tempBaseDir);

        File newFile = new File(tempBaseDir, "newfile.txt");
        boolean result = FileUtil.dirRename(file.getPath(), newFile.getPath());
        assertTrue(result);
        assertFalse(file.exists());
        assertTrue(newFile.exists());
    }

    // ==================== getFileType (magic bytes) ====================

    @Test
    public void testGetFileType() throws Exception {
        // PNG magic bytes
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        };
        File png = new File(tempBaseDir, "test.png");
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(png)) {
            fos.write(pngData);
        }
        assertEquals("png", FileUtil.getFileType(png));
    }

    // ==================== isImage ====================

    @Test
    public void testIsImage() throws Exception {
        File png = new File(tempBaseDir, "test-is-image.png");
        byte[] pngData = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        };
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(png)) {
            fos.write(pngData);
        }
        assertTrue(FileUtil.isImage(png.getPath()));
    }

    @Test
    public void testIsImageFalseForTxt() throws Exception {
        File txt = File.createTempFile("test-text", ".txt", tempBaseDir);
        assertFalse(FileUtil.isImage(txt.getPath()));
    }

    // ==================== CopyFile ====================

    @Test
    public void testCopyFile() throws Exception {
        File src = new File(tempBaseDir, "src-copy.txt");
        Files.write(src.toPath(), "copy source".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest-copy.txt");
        boolean result = FileUtil.CopyFile(src, dest);

        assertTrue(result);
        assertEquals("copy source", FileUtil.getFileContent(dest));
    }

    @Test
    public void testCopyFileWithChannel() throws Exception {
        File src = new File(tempBaseDir, "src-channel.txt");
        Files.write(src.toPath(), "channel copy".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest-channel.txt");
        FileUtil.copyFileWithChannel(src, dest);

        assertEquals("channel copy", FileUtil.getFileContent(dest));
    }

    @Test
    public void testCopyFileWithBuffer() throws Exception {
        File src = new File(tempBaseDir, "src-buffer.txt");
        Files.write(src.toPath(), "buffer copy".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest-buffer.txt");
        FileUtil.copyFileWithBuffer(src, dest);

        assertEquals("buffer copy", FileUtil.getFileContent(dest));
    }

    // ==================== getURL ====================

    @Test
    public void testGetURL() throws Exception {
        File f = File.createTempFile("urltest", ".txt", tempBaseDir);
        URL url = FileUtil.getURL(f);
        assertNotNull(url);
    }

    // ==================== convert charset ====================

    @Test
    public void testConvert() throws Exception {
        File src = new File(tempBaseDir, "src-gbk.txt");
        Files.write(src.toPath(), "转换测试".getBytes(java.nio.charset.Charset.forName("GBK")));

        File dest = new File(tempBaseDir, "dest-utf8.txt");
        FileUtil.convert(src, "GBK", "UTF-8");
    }

    // ==================== genModuleTpl ====================

    @Test
    public void testGenModuleTpl() throws Exception {
        File f = new File(tempBaseDir, "module.txt");
        boolean result = FileUtil.genModuleTpl(f.getPath(), "module content here");
        assertTrue(result);
        assertEquals("module content here", FileUtil.getFileContent(f));
    }

    // ==================== listAll ====================

    @Test
    public void testListAll() throws Exception {
        File dir = new File(tempBaseDir, "listall");
        dir.mkdirs();
        File f1 = new File(dir, "file1.txt");
        File f2 = new File(dir, "file2.java");
        f1.createNewFile();
        f2.createNewFile();

        File[] all = FileUtil.listAll(dir, null);
        assertEquals(2, all.length);
    }

    // ==================== getPicExtendName ====================

    @Test
    public void testGetPicExtendName() throws Exception {
        File png = new File(tempBaseDir, "test.png");
        png.createNewFile();
        assertEquals("png", FileUtil.getPicExtendName(png.getPath()));
    }

    @Test
    public void testGetPicExtendNameNoExtension() throws Exception {
        File f = new File(tempBaseDir, "noextension");
        f.createNewFile();
        assertNull(FileUtil.getPicExtendName(f.getPath()));
    }
}
