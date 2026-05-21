package com.zifang.util.proxy.a.decompile.bean.constant;

/**
 * 字符串常量类
 * <p>
 * CONSTANT_String_info用于表示字符串常量的符号引用。
 */
public class Constant_String_info extends Constant_X_info {
    public static final int tag_length = 1;
    public static final int index_length = 2;

    private String tag;
    private int index;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
