package com.zifang.util.core.io.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

/**
 * JAR 包解压工具类
 */
public class JarUtil {

    /**
     * 解压 JAR 文件到指定目录
     *
     * @param jarPath JAR 文件路径
     * @param targetDir 目标目录
     * @throws IOException 如果解压失败
     */
    public static void unPack(String jarPath, String targetDir) throws IOException {
        File jarFile = new File(jarPath);
        File destinationDir = new File(targetDir);

        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream().forEach(entry -> {
                String name = entry.getName();
                File targetFile = new File(destinationDir, name);
                try {
                    if (entry.isDirectory()) {
                        targetFile.mkdirs();
                    } else {
                        targetFile.getParentFile().mkdirs();
                        try (InputStream is = jar.getInputStream(entry);
                             FileOutputStream fos = new FileOutputStream(targetFile)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = is.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private static class UncheckedIOException extends RuntimeException {
        private UncheckedIOException(IOException cause) {
            super(cause);
        }
    }
}
