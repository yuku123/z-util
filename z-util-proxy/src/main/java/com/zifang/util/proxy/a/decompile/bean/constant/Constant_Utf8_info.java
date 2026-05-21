package com.zifang.util.proxy.a.decompile.bean.constant;

/**
 * UTF-8字符串常量类
 * <p>
 * CONSTANT_Utf8_info用于存储字符串、类名、方法名等文本数据。
 */
public class Constant_Utf8_info extends Constant_X_info {

    public static final int tag_length = 1;
    public static final int length_length = 2;
    public static final int bytes_length = 1;


    private String tag;
    private int length;
    private String bytes;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }


}
