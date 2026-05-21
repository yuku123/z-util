package com.zifang.util.proxy.a.resolver2.readtype;

import java.io.IOException;
import java.io.InputStream;

/**
 * 无符号1字节数据类型
 * <p>
 * 用于从输入流中读取1字节的无符号整数，范围为0-255。
 */
public class U1 {

    public byte value;

    public U1(byte value) {
        this.value = value;
    }

    public static U1 read(InputStream stream) {
        byte[] bytes = new byte[1];
        try {
            stream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        U1 u1 = new U1(bytes[0]);
        return u1;
    }

    public byte getValue() {
        return value;
    }
}
