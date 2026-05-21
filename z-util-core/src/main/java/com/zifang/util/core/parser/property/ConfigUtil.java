package com.zifang.util.core.parser.property;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 配置资源加载工具类，按 ClassLoader 顺序查找资源
 */
public class ConfigUtil {

    /**
     * 查找配置文件资源，按以下顺序尝试：
     * 1. Thread.currentThread().getContextClassLoader()
     * 2. ConfigUtil.class.getClassLoader()
     * 3. ClassLoader.getSystemClassLoader()
     *
     * @param path 资源路径（相对路径或绝对路径）
     * @return 资源 URL，未找到返回 null
     */
    public static URL findAsResource(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            URL url = cl.getResource(path);
            if (url != null) {
                return url;
            }
        }
        URL url = ConfigUtil.class.getClassLoader().getResource(path);
        if (url != null) {
            return url;
        }
        return ClassLoader.getSystemClassLoader().getResource(path);
    }

    /**
     * 将资源路径转为文件系统路径
     *
     * @param path 资源路径
     * @return 文件系统路径
     */
    public static String toFilePath(String path) {
        URL url = findAsResource(path);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new File(url.getFile()).getPath();
    }

    /**
     * 从资源路径加载 Properties
     *
     * @param path 资源路径
     * @return Properties 对象
     * @throws IOException 如果读取失败
     */
    public static Properties loadProperties(String path) throws IOException {
        URL url = findAsResource(path);
        if (url == null) {
            throw new IOException("Resource not found: " + path);
        }
        Properties properties = new Properties();
        try (InputStream is = url.openStream()) {
            properties.load(is);
        }
        return properties;
    }
}
