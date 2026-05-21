package com.zifang.util.proxy.a.decompile.bean.constant;

/**
 * 浮点数常量类
 * <p>
 * CONSTANT_Float_info用于表示4字节的float类型常量值。
 */
public class Constant_Float_info extends Constant_X_info {
    public static final int tag_length = 1;
    public static final int bytes_length = 4;

    private String tag;
    private String bytes;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

}
