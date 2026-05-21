package com.zifang.util.core.io.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class FileCopyUtilTest {

    private File tempBaseDir;

    @Before
    public void setUp() throws Exception {
        tempBaseDir = Files.createTempDirectory("copy-test-base").toFile();
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

    // ==================== copyFile ====================

    @Test
    public void testCopyFileBasic() throws Exception {
        File src = new File(tempBaseDir, "src.txt");
        Files.write(src.toPath(), "hello world".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest.txt");
        FileCopyUtil.copyFile(src, dest);

        assertTrue(dest.exists());
        assertEquals("hello world", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    @Test
    public void testCopyFileOverwritesExisting() throws Exception {
        File src = new File(tempBaseDir, "src2.txt");
        Files.write(src.toPath(), "new content".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest2.txt");
        Files.write(dest.toPath(), "old content".getBytes(StandardCharsets.UTF_8));

        FileCopyUtil.copyFile(src, dest);
        assertEquals("new content", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    @Test(expected = NullPointerException.class)
    public void testCopyFileWithNullSrc() throws Exception {
        File dest = new File(tempBaseDir, "dest-null.txt");
        FileCopyUtil.copyFile(null, dest);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyFileWithNullDest() throws Exception {
        File src = new File(tempBaseDir, "src-null.txt");
        src.createNewFile();
        FileCopyUtil.copyFile(src, null);
    }

    @Test
    public void testCopyFileWithSubdirCreation() throws Exception {
        File src = new File(tempBaseDir, "src-subdir.txt");
        Files.write(src.toPath(), "content".getBytes(StandardCharsets.UTF_8));

        File dest = new File(new File(tempBaseDir, "sub"), "dest.txt");
        FileCopyUtil.copyFile(src, dest);

        assertTrue(dest.exists());
        assertEquals("content", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    // ==================== copyFile with overwrite flag ====================

    @Test
    public void testCopyFileNoOverwrite() throws Exception {
        File src = new File(tempBaseDir, "src3.txt");
        Files.write(src.toPath(), "source".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest3.txt");
        Files.write(dest.toPath(), "dest".getBytes(StandardCharsets.UTF_8));

        FileCopyUtil.copyFile(src, dest, false);
        // dest should be unchanged
        assertEquals("dest", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    @Test(expected = java.nio.file.FileAlreadyExistsException.class)
    public void testCopyFileNoOverwriteThrows() throws Exception {
        File src = new File(tempBaseDir, "src4.txt");
        Files.write(src.toPath(), "source".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "dest4.txt");
        Files.write(dest.toPath(), "dest".getBytes(StandardCharsets.UTF_8));

        FileCopyUtil.copyFile(src, dest, false);
    }

    // ==================== copyFileToDir ====================

    @Test
    public void testCopyFileToDir() throws Exception {
        File src = new File(tempBaseDir, "src5.txt");
        Files.write(src.toPath(), "hello".getBytes(StandardCharsets.UTF_8));

        File destDir = new File(tempBaseDir, "destdir5");
        destDir.mkdirs();

        FileCopyUtil.copyFileToDir(src, destDir);

        File dest = new File(destDir, "src5.txt");
        assertTrue(dest.exists());
        assertEquals("hello", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    // ==================== copyDir ====================

    @Test
    public void testCopyDirBasic() throws Exception {
        File srcDir = new File(tempBaseDir, "srcdir");
        srcDir.mkdirs();
        File srcFile1 = new File(srcDir, "file1.txt");
        File srcFile2 = new File(srcDir, "file2.txt");
        Files.write(srcFile1.toPath(), "content1".getBytes(StandardCharsets.UTF_8));
        Files.write(srcFile2.toPath(), "content2".getBytes(StandardCharsets.UTF_8));

        File destDir = new File(tempBaseDir, "destdir");
        FileCopyUtil.copyDir(srcDir, destDir);

        assertTrue(destDir.exists());
        assertTrue(new File(destDir, "file1.txt").exists());
        assertTrue(new File(destDir, "file2.txt").exists());
        assertEquals("content1", new String(Files.readAllBytes(new File(destDir, "file1.txt").toPath()), StandardCharsets.UTF_8));
    }

    @Test
    public void testCopyDirWithSubdirs() throws Exception {
        File srcDir = new File(tempBaseDir, "srcdir2");
        srcDir.mkdirs();
        File subDir = new File(srcDir, "subdir");
        subDir.mkdirs();
        File srcFile = new File(subDir, "nested.txt");
        Files.write(srcFile.toPath(), "nested content".getBytes(StandardCharsets.UTF_8));

        File destDir = new File(tempBaseDir, "destdir2");
        FileCopyUtil.copyDir(srcDir, destDir);

        assertTrue(new File(destDir, "subdir/nested.txt").exists());
        assertEquals("nested content", new String(Files.readAllBytes(new File(destDir, "subdir/nested.txt").toPath()), StandardCharsets.UTF_8));
    }

    @Test(expected = NullPointerException.class)
    public void testCopyDirWithNullSrc() throws Exception {
        File destDir = new File(tempBaseDir, "dest-null-dir");
        FileCopyUtil.copyDir(null, destDir);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyDirWithNullDest() throws Exception {
        File srcDir = new File(tempBaseDir, "src-null-dir");
        srcDir.mkdirs();
        FileCopyUtil.copyDir(srcDir, null);
    }

    // ==================== moveFile ====================

    @Test
    public void testMoveFile() throws Exception {
        File src = new File(tempBaseDir, "move-src.txt");
        Files.write(src.toPath(), "to be moved".getBytes(StandardCharsets.UTF_8));

        File dest = new File(tempBaseDir, "move-dest.txt");
        FileCopyUtil.moveFile(src, dest);

        assertFalse(src.exists());
        assertTrue(dest.exists());
        assertEquals("to be moved", new String(Files.readAllBytes(dest.toPath()), StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testMoveFileSrcNotExists() throws Exception {
        File src = new File(tempBaseDir, "non-existent-move.txt");
        File dest = new File(tempBaseDir, "move-dest2.txt");
        FileCopyUtil.moveFile(src, dest);
    }

    // ==================== moveDir ====================

    @Test
    public void testMoveDir() throws Exception {
        File srcDir = new File(tempBaseDir, "move-src-dir");
        srcDir.mkdirs();
        File srcFile = new File(srcDir, "file.txt");
        Files.write(srcFile.toPath(), "dir content".getBytes(StandardCharsets.UTF_8));

        File destDir = new File(tempBaseDir, "move-dest-dir");
        FileCopyUtil.moveDir(srcDir, destDir);

        assertFalse(srcDir.exists());
        assertTrue(destDir.exists());
        assertTrue(new File(destDir, "file.txt").exists());
    }

    @Test(expected = IOException.class)
    public void testMoveDirSrcNotExists() throws Exception {
        File srcDir = new File(tempBaseDir, "non-existent-move-dir");
        File destDir = new File(tempBaseDir, "move-dest-dir2");
        FileCopyUtil.moveDir(srcDir, destDir);
    }

    // ==================== copyStream ====================

    @Test
    public void testCopyStreamBasic() throws Exception {
        byte[] data = "stream content".getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(data);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        long count = FileCopyUtil.copyStream(in, out);

        assertEquals(data.length, count);
        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testCopyStreamEmpty() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        long count = FileCopyUtil.copyStream(in, out);

        assertEquals(0, count);
        assertArrayEquals(new byte[0], out.toByteArray());
    }

    @Test
    public void testCopyStreamLarge() throws Exception {
        byte[] large = new byte[100_000];
        for (int i = 0; i < large.length; i++) {
            large[i] = (byte) (i % 256);
        }
        InputStream in = new ByteArrayInputStream(large);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        long count = FileCopyUtil.copyStream(in, out);

        assertEquals(large.length, count);
        assertArrayEquals(large, out.toByteArray());
    }

    @Test(expected = NullPointerException.class)
    public void testCopyStreamWithNullInput() throws Exception {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        FileCopyUtil.copyStream(null, out);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyStreamWithNullOutput() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        FileCopyUtil.copyStream(in, null);
    }
}
