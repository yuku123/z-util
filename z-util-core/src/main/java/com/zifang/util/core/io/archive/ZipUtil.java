package com.zifang.util.core.io.archive;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZIP 压缩/解压工具类
 * <p>
 * 提供 ZIP 格式文件的压缩和解压功能，支持：
 * <ul>
 *   <li>将文件或文件夹压缩为 ZIP 格式</li>
 *   <li>解压 ZIP 文件到指定目录</li>
 *   <li>压缩单个文件到指定 ZIP 包</li>
 * </ul>
 * <p>
 * 示例用法：
 * <pre>{@code
 * // 压缩文件夹
 * ZipUtil.zipFolder("/path/to/folder", "/target/dir", "result.zip");
 *
 * // 解压 ZIP 文件
 * ZipUtil.unZip("/path/to/file.zip", "/target/dir");
 * }</pre>
 *
 * @author zifang
 * @see ZipFile
 * @see ZipOutputStream
 */
/**
 * ZipUtil类。
 */
/**
 * ZipUtil类。
 */
public class ZipUtil {

    private static final String ZIP_FILE_SUFFIX = ".zip";
    private static final int BUFFER_SIZE = 1024;

    /**
     * 压缩文件或文件夹为 ZIP 格式
     * <p>
     * 将指定路径的文件或文件夹压缩为同名的 ZIP 文件，输出到目标文件夹。
     * 例如：zipFile("/home/user/docs", "/home/user/archives") 会生成 "/home/user/archives/docs.zip"
     *
     * @param resourcePath 源文件或文件夹路径（不能为空）
     * @param targetPath  目标文件夹路径（如果不存在会自动创建）
     * @throws IllegalArgumentException 如果 resourcePath 或 targetPath 为空
     * @throws UncheckedIOException    如果压缩过程中发生 IO 错误
     */
    /**
     * zipFile方法。
     *      * @param resourcePath String类型参数
     * @param targetPath String类型参数
     * @return static void类型返回值
     */
    /**
     * zipFile方法。
     *      * @param resourcePath String类型参数
     * @param targetPath String类型参数
     * @return static void类型返回值
     */
    public static void zipFile(String resourcePath, String targetPath) {
        File resourcesFile = new File(resourcePath);
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        String targetName = resourcesFile.getName() + ZIP_FILE_SUFFIX;

        try (ZipOutputStream out = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(targetPath + "/" + targetName)))) {
            compressedFile(out, resourcesFile, "");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 压缩文件夹为指定名称的 ZIP 文件
     * <p>
     * 将文件夹压缩为指定名称的 ZIP 文件，支持自定义输出路径和文件名。
     * 文件名如果已包含 .zip 扩展名则直接使用，否则自动添加。
     *
     * @param folder       要压缩的文件夹路径（必须是存在的目录）
     * @param targetFolder 目标文件夹路径（如果不存在会自动创建）
     * @param zipFileName  ZIP 文件名（可包含或不包含 .zip 扩展名）
     * @throws IOException              如果 folder 不是目录或不存在
     * @throws IllegalArgumentException 如果参数为空
     */
    /**
     * zipFolder方法。
     *      * @param folder String类型参数
     * @param targetFolder String类型参数
     * @param zipFileName String类型参数
     * @return static void类型返回值
     */
    /**
     * zipFolder方法。
     *      * @param folder String类型参数
     * @param targetFolder String类型参数
     * @param zipFileName String类型参数
     * @return static void类型返回值
     */
    public static void zipFolder(String folder, String targetFolder, String zipFileName) throws IOException {
        File zipFolder = new File(folder);

        if (!zipFolder.isDirectory()) {
            throw new IOException("folder: " + folder + " is not a Folder");
        }
        if (!zipFolder.exists()) {
            throw new IOException("folder: " + folder + " does not exist");
        }

        File targetDir = new File(targetFolder);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        String zipFilePath = zipFileName.endsWith(ZIP_FILE_SUFFIX)
                ? targetFolder + File.separator + zipFileName
                : targetFolder + File.separator + zipFileName + ZIP_FILE_SUFFIX;

        try (ZipOutputStream out = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(zipFilePath)))) {
            compressedFile(out, zipFolder, zipFolder.getName());
        }
    }

    /**
     * 压缩单个文件到目标 ZIP 文件
     * <p>
     * 将指定文件压缩到目标 ZIP 文件中，并指定文件在 ZIP 包中的名称。
     * 如果目标 ZIP 文件已存在，则追加到该文件；如果不存在，则创建新文件。
     *
     * @param srcFilePath     源文件路径（必须是存在的文件）
     * @param destZipFilePath 目标 ZIP 文件路径（目录必须存在）
     * @param fileName        文件在 ZIP 包中的名称（通常与源文件名相同）
     * @throws IllegalArgumentException 如果任何参数为空
     * @throws UncheckedIOException     如果压缩过程中发生 IO 错误
     */
    /**
     * zipFile方法。
     *      * @param srcFilePath String类型参数
     * @param destZipFilePath String类型参数
     * @param fileName String类型参数
     * @return static void类型返回值
     */
    /**
     * zipFile方法。
     *      * @param srcFilePath String类型参数
     * @param destZipFilePath String类型参数
     * @param fileName String类型参数
     * @return static void类型返回值
     */
    public static void zipFile(String srcFilePath, String destZipFilePath, String fileName) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFilePath));
             FileInputStream fis = new FileInputStream(srcFilePath)) {
            ZipEntry z1 = new ZipEntry(fileName);
            zos.putNextEntry(z1);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 解压 ZIP 文件到指定目录
     * <p>
     * 将 ZIP 文件解压到目标目录。解压时保持 ZIP 内的目录结构。
     * 如果目标目录不存在会自动创建。
     *
     * @param zipFilePath ZIP 文件路径（必须是存在的 .zip 文件）
     * @param destDir     解压目标目录（如果不存在会自动创建）
     * @throws IOException              如果 ZIP 文件不存在或格式无效
     * @throws IllegalArgumentException 如果任何参数为空
     */
    /**
     * unZip方法。
     *      * @param zipFilePath String类型参数
     * @param destDir String类型参数
     * @return static void类型返回值
     */
    /**
     * unZip方法。
     *      * @param zipFilePath String类型参数
     * @param destDir String类型参数
     * @return static void类型返回值
     */
    public static void unZip(String zipFilePath, String destDir) throws IOException {
        File f = new File(destDir);
        if (!f.exists()) {
            f.mkdirs();
        }

        try (ZipFile zipfile = new ZipFile(zipFilePath)) {
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File destFile = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    destFile.getParentFile().mkdirs();
                    try (BufferedInputStream is = new BufferedInputStream(zipfile.getInputStream(entry));
                         BufferedOutputStream fos = new BufferedOutputStream(
                                 new FileOutputStream(destFile), BUFFER_SIZE)) {
                        byte[] data = new byte[BUFFER_SIZE];
                        int count;
                        while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
                            fos.write(data, 0, count);
                        }
                    }
                }
            }
        }
    }

    /**
     * 递归压缩文件或文件夹
     *
     * @param out  ZIP 输出流
     * @param file 要压缩的文件或文件夹
     * @param dir  当前条目在 ZIP 包中的路径前缀
     * @throws IOException 如果压缩过程中发生 IO 错误
     */
    private static void compressedFile(ZipOutputStream out, File file, String dir) throws IOException {
        if (file.isDirectory()) {
            String childDir = dir.isEmpty() ? "" : dir + "/";
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    compressedFile(out, child, childDir + child.getName());
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                out.putNextEntry(new ZipEntry(dir));
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    /**
     * 非受检 IO 异常包装器
     * <p>
     * 将 IOException 包装为 RuntimeException，方便在不支持抛出检查异常的场合使用。
     */
    private static class UncheckedIOException extends RuntimeException {
        private UncheckedIOException(IOException cause) {
            super(cause);
        }
    }
}
