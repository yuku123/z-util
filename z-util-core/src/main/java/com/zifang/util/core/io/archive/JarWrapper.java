package com.zifang.util.core.io.archive;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * JAR 包业务级操作封装，提供按路径读取 entry、按条件过滤等高级操作
 */
public class JarWrapper implements Closeable {

    private final JarFile jarFile;

    /**
     * @param jarFilePath JAR 文件路径
     */
    public JarWrapper(String jarFilePath) throws IOException {
        this.jarFile = new JarFile(jarFilePath);
    }

    /**
     * @param jarFilePath JAR 文件
     */
    public JarWrapper(File jarFilePath) throws IOException {
        this.jarFile = new JarFile(jarFilePath);
    }

    /**
     * 获取所有 JAR entry
     */
    public List<JarEntry> getEntries() {
        List<JarEntry> jarEntries = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            jarEntries.add(entries.nextElement());
        }
        return jarEntries;
    }

    /**
     * 根据路径读取 JAR 中指定 entry 的字节数组
     *
     * @param pathInJar JAR 内的路径，如 "META-INF/MANIFEST.MF"
     * @return entry 内容字节数组，找不到时返回空数组
     */
    public byte[] getBytes(String pathInJar) {
        try {
            JarEntry entry = jarFile.getJarEntry(pathInJar);
            if (entry == null) {
                return new byte[0];
            }
            return toByteArray(jarFile.getInputStream(entry));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 根据路径读取 JAR 中指定 entry 的字符串内容（UTF-8）
     *
     * @param pathInJar JAR 内的路径
     * @return entry 文本内容，找不到时返回空字符串
     */
    public String getString(String pathInJar) {
        byte[] bytes = getBytes(pathInJar);
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * 按条件过滤 JAR entries，并返回路径到字节数组的映射
     *
     * @param predicate 过滤条件
     * @return 符合条件的 path -> bytes 映射（保持枚举顺序）
     */
    public Map<String, byte[]> getFileBytes(Predicate<JarEntry> predicate) {
        Map<String, byte[]> map = new LinkedHashMap<>();
        List<JarEntry> filtered = getEntries().stream()
                .filter(predicate)
                .collect(Collectors.toList());

        for (JarEntry entry : filtered) {
            map.put(entry.getName(), getBytes(entry.getName()));
        }
        return map;
    }

    /**
     * 关闭 JAR 文件
     */
    @Override
    public void close() throws IOException {
        if (jarFile != null) {
            jarFile.close();
        }
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private static class UncheckedIOException extends RuntimeException {
        private UncheckedIOException(IOException cause) {
            super(cause);
        }
    }
}
