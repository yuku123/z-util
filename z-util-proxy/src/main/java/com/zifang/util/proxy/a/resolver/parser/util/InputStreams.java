package com.zifang.util.proxy.a.resolver.parser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 输入流工具类<br>
 * 提供文件输入流的创建方法
 */
public class InputStreams {

    /**
     * 根据文件创建文件输入流
     *
     * @param file 文件对象
     * @return 文件输入流
     * @throws RuntimeException 如果文件未找到
     */
    public static FileInputStream fileInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
