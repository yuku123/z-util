package com.zifang.util.proxy.a.decompile.bean.constant;

/**
 * 字段引用常量类
 * <p>
 * CONSTANT_Fieldref_info用于表示类中声明的字段的符号引用。
 */
public class Constant_Fieldref_info extends Constant_X_info {
    public static final int tag_length = 1;
    public static final int index_length = 2;
    public static final int index2_length = 2;

    private String tag;
    private int index;
    private int index2;

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

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }


}
