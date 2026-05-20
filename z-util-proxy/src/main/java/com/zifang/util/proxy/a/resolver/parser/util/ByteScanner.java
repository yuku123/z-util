package com.zifang.util.proxy.a.resolver.parser.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 字节扫描器<br>
 * 用于从输入流中读取不同长度的字节数据并转换为整数或字符串
 */
public class ByteScanner {

    private InputStream inputStream;

    /**
     * 构造方法
     *
     * @param inputStream 输入流
     */
    public ByteScanner(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 从输入流读取指定长度的字节并转换为整数
     * <p>
     * 支持1、2、4字节长度的读取，采用高位在前（Big-Endian）存储方式
     *
     * @param size 字节长度，支持1、2、4
     * @return 转换后的整数值，读取失败返回-1
     */
    public int readToInteger(int size) {
        byte[] b = new byte[size];
        try {
            this.inputStream.read(b);
            if (size == 1) {
                return b[0] & 0x000000ff;
            } else if (size == 2) {
                return b[0] << 8 & 0x0000ff00 | b[1] & 0x000000ff;
            } else if (size == 4) {
                return b[0] << 24 & 0xff000000 | b[1] << 16 & 0x00ff0000 | b[2] << 8 & 0x0000ff00 | b[3] & 0x000000ff;
            } else {
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 从输入流读取8个字节并转换为长整数
     * <p>
     * 采用高位在前（Big-Endian）存储方式
     *
     * @return 转换后的长整数值，读取失败返回-1
     */
    public long readToLong() {
        byte[] bytes = new byte[8];
        try {
            this.inputStream.read(bytes);
            return bytes[0] << 56 & 0xff00000000000000L | bytes[1] << 48 & 0x00ff000000000000L
                    | bytes[2] << 40 & 0x0000ff0000000000L | bytes[3] << 32 & 0x000000ff00000000L
                    | bytes[4] << 24 & 0x00000000ff000000L | bytes[5] << 16 & 0x0000000000ff0000L
                    | bytes[6] << 8 & 0x0000000000ff00L | bytes[7] & 0x00000000000000ffL;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 从输入流读取指定长度的字节并转换为字符串
     *
     * @param size 要读取的字节长度
     * @return 转换后的字符串，读取失败返回null
     */
    public String readToString(int size) {
        byte[] b = new byte[size];
        try {
            this.inputStream.read(b);
            return new String(b).intern();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
