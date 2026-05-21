package com.zifang.util.core.io.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class FileDirUtilTest {

    private File tempBaseDir;

    @Before
    public void setUp() throws Exception {
        tempBaseDir = Files.createTempDirectory("dir-test-base").toFile();
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

    // ==================== mkdir ====================

    @Test
    public void testMkdirCreatesDirectory() throws Exception {
        File newDir = new File(tempBaseDir, "newdir");
        FileDirUtil.mkdir(newDir);
        assertTrue(newDir.exists());
        assertTrue(newDir.isDirectory());
    }

    @Test
    public void testMkdirFromPathString() throws Exception {
        File newDir = new File(tempBaseDir, "newdir2");
        FileDirUtil.mkdir(newDir.getPath());
        assertTrue(newDir.exists());
        assertTrue(newDir.isDirectory());
    }

    @Test
    public void testMkdirDoesNotThrowWhenExists() throws Exception {
        File existingDir = new File(tempBaseDir, "existing");
        existingDir.mkdirs();
        FileDirUtil.mkdir(existingDir); // should not throw
        assertTrue(existingDir.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testMkdirWithNullPath() throws Exception {
        FileDirUtil.mkdir((String) null);
    }

    @Test(expected = IOException.class)
    public void testMkdirWithEmptyPath() throws Exception {
        FileDirUtil.mkdir("");
    }

    // ==================== mkdirs ====================

    @Test
    public void testMkdirsCreatesNestedDirectories() throws Exception {
        File nested = new File(tempBaseDir, "a/b/c/d");
        FileDirUtil.mkdirs(nested.getAbsolutePath());
        assertTrue(nested.exists());
        assertTrue(nested.isDirectory());
    }

    @Test
    public void testMkdirsFromPathString() throws Exception {
        File nested = new File(tempBaseDir, "x/y/z");
        FileDirUtil.mkdirs(nested.getPath());
        assertTrue(nested.exists());
        assertTrue(nested.isDirectory());
    }

    // ==================== cleanDir ====================

    @Test
    public void testCleanDirRemovesFiles() throws Exception {
        File dir = new File(tempBaseDir, "cleanme");
        dir.mkdirs();
        new File(dir, "file1.txt").createNewFile();
        new File(dir, "file2.txt").createNewFile();

        FileDirUtil.cleanDir(dir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        assertEquals(0, dir.listFiles().length);
    }

    @Test
    public void testCleanDirRemovesSubdirs() throws Exception {
        File dir = new File(tempBaseDir, "cleansub");
        dir.mkdirs();
        File subDir = new File(dir, "subdir");
        subDir.mkdirs();
        new File(subDir, "file.txt").createNewFile();

        FileDirUtil.cleanDir(dir);
        assertTrue(dir.exists());
        assertEquals(0, dir.listFiles().length);
    }

    @Test(expected = IOException.class)
    public void testCleanDirWithNull() throws Exception {
        FileDirUtil.cleanDir(null);
    }

    @Test(expected = IOException.class)
    public void testCleanDirWithNonExistent() throws Exception {
        FileDirUtil.cleanDir(new File(tempBaseDir, "non-existent"));
    }

    // ==================== deleteDir ====================

    @Test
    public void testDeleteDirRemovesDirAndContents() throws Exception {
        File dir = new File(tempBaseDir, "deleteme");
        dir.mkdirs();
        new File(dir, "file.txt").createNewFile();
        File subDir = new File(dir, "subdir");
        subDir.mkdirs();
        new File(subDir, "file2.txt").createNewFile();

        FileDirUtil.deleteDir(dir);
        assertFalse(dir.exists());
    }

    @Test
    public void testDeleteDirDoesNotThrowIfNotExists() throws Exception {
        File nonExistent = new File(tempBaseDir, "non-existent-dir");
        FileDirUtil.deleteDir(nonExistent); // should not throw
    }

    @Test(expected = IOException.class)
    public void testDeleteDirWithNull() throws Exception {
        FileDirUtil.deleteDir((File) null);
    }

    // ==================== deleteFile ====================

    @Test
    public void testDeleteFileRemovesFile() throws Exception {
        File file = new File(tempBaseDir, "deletefile.txt");
        file.createNewFile();
        FileDirUtil.deleteFile(file);
        assertFalse(file.exists());
    }

    @Test
    public void testDeleteFileDoesNotThrowIfNotExists() throws Exception {
        File nonExistent = new File(tempBaseDir, "non-existent-file.txt");
        FileDirUtil.deleteFile(nonExistent); // should not throw
    }

    @Test(expected = IOException.class)
    public void testDeleteFileWithNull() throws Exception {
        FileDirUtil.deleteFile((File) null);
    }

    // ==================== isEmptyDir ====================

    @Test
    public void testIsEmptyDirWithEmptyDir() throws Exception {
        File emptyDir = new File(tempBaseDir, "empty");
        emptyDir.mkdirs();
        assertTrue(FileDirUtil.isEmptyDir(emptyDir));
    }

    @Test
    public void testIsEmptyDirWithNonEmptyDir() throws Exception {
        File dir = new File(tempBaseDir, "nonempty");
        dir.mkdirs();
        new File(dir, "file.txt").createNewFile();
        assertFalse(FileDirUtil.isEmptyDir(dir));
    }

    @Test
    public void testIsEmptyDirWithFile() throws Exception {
        File file = new File(tempBaseDir, "afile.txt");
        file.createNewFile();
        assertFalse(FileDirUtil.isEmptyDir(file));
    }

    @Test(expected = IOException.class)
    public void testIsEmptyDirWithNull() throws Exception {
        FileDirUtil.isEmptyDir(null);
    }

    // ==================== listFiles ====================

    @Test
    public void testListFilesReturnsFiles() throws Exception {
        File dir = new File(tempBaseDir, "listfiles");
        dir.mkdirs();
        File file1 = new File(dir, "file1.txt");
        File file2 = new File(dir, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();

        List<File> files = FileDirUtil.listFiles(dir);
        assertEquals(2, files.size());
    }

    @Test
    public void testListFilesWithEmptyDir() throws Exception {
        File emptyDir = new File(tempBaseDir, "empty-list");
        emptyDir.mkdirs();
        List<File> files = FileDirUtil.listFiles(emptyDir);
        assertEquals(0, files.size());
    }

    @Test(expected = IOException.class)
    public void testListFilesWithNull() throws Exception {
        FileDirUtil.listFiles(null);
    }

    // ==================== listFiles with extension filter ====================

    @Test
    public void testListFilesWithExtension() throws Exception {
        File dir = new File(tempBaseDir, "extfilter");
        dir.mkdirs();
        new File(dir, "file1.txt").createNewFile();
        new File(dir, "file2.java").createNewFile();
        new File(dir, "file3.txt").createNewFile();

        List<File> txtFiles = FileDirUtil.listFiles(dir, "txt");
        assertEquals(2, txtFiles.size());
    }

    @Test
    public void testListFilesWithExtensionNoMatch() throws Exception {
        File dir = new File(tempBaseDir, "extnomatch");
        dir.mkdirs();
        new File(dir, "file1.txt").createNewFile();
        List<File> xmlFiles = FileDirUtil.listFiles(dir, "xml");
        assertEquals(0, xmlFiles.size());
    }

    // ==================== listFilesRecursively ====================

    @Test
    public void testListFilesRecursively() throws Exception {
        File dir = new File(tempBaseDir, "recursive");
        dir.mkdirs();
        new File(dir, "file1.txt").createNewFile();
        File subDir = new File(dir, "subdir");
        subDir.mkdirs();
        new File(subDir, "file2.txt").createNewFile();

        List<File> files = FileDirUtil.listFilesRecursively(dir);
        assertEquals(2, files.size());
    }

    @Test
    public void testListFilesRecursivelyEmptyDir() throws Exception {
        File emptyDir = new File(tempBaseDir, "empty-recursive");
        emptyDir.mkdirs();
        List<File> files = FileDirUtil.listFilesRecursively(emptyDir);
        assertEquals(0, files.size());
    }

    @Test(expected = IOException.class)
    public void testListFilesRecursivelyWithNull() throws Exception {
        FileDirUtil.listFilesRecursively(null);
    }

    // ==================== searchFile ====================

    @Test
    public void testSearchFileFindsFile() throws Exception {
        File dir = new File(tempBaseDir, "search");
        dir.mkdirs();
        File target = new File(dir, "target.txt");
        target.createNewFile();

        File found = FileDirUtil.searchFile(dir, "target.txt");
        assertNotNull(found);
        assertEquals(target, found);
    }

    @Test
    public void testSearchFileInSubdir() throws Exception {
        File dir = new File(tempBaseDir, "searchsub");
        dir.mkdirs();
        File subDir = new File(dir, "subdir");
        subDir.mkdirs();
        File target = new File(subDir, "target.txt");
        target.createNewFile();

        File found = FileDirUtil.searchFile(dir, "target.txt");
        assertNotNull(found);
        assertEquals(target, found);
    }

    @Test
    public void testSearchFileNotFound() throws Exception {
        File dir = new File(tempBaseDir, "notfound");
        dir.mkdirs();
        File found = FileDirUtil.searchFile(dir, "nonexistent.txt");
        assertNull(found);
    }

    @Test(expected = IOException.class)
    public void testSearchFileWithNullDir() throws Exception {
        FileDirUtil.searchFile(null, "file.txt");
    }

    @Test(expected = IOException.class)
    public void testSearchFileWithNullName() throws Exception {
        FileDirUtil.searchFile(tempBaseDir, null);
    }

    // ==================== ensureDirExists ====================

    @Test
    public void testEnsureDirExistsCreatesNewDir() throws Exception {
        File newDir = new File(tempBaseDir, "ensure-new");
        FileDirUtil.ensureDirExists(newDir);
        assertTrue(newDir.exists());
    }

    @Test
    public void testEnsureDirExistsReturnsExistingDir() throws Exception {
        File existing = new File(tempBaseDir, "ensure-existing");
        existing.mkdirs();
        FileDirUtil.ensureDirExists(existing);
        assertTrue(existing.exists());
    }

    @Test
    public void testEnsureDirExistsCreatesNestedDirs() throws Exception {
        File nested = new File(tempBaseDir, "ensure/nested/dir");
        FileDirUtil.ensureDirExists(nested);
        assertTrue(nested.exists());
    }

    @Test(expected = IOException.class)
    public void testEnsureDirExistsWithNull() throws Exception {
        FileDirUtil.ensureDirExists(null);
    }
}
