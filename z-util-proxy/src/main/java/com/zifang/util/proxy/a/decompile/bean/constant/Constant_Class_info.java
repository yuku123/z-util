package com.zifang.util.proxy.a.decompile.bean.constant;

/**
 * 类引用常量类
 * <p>
 * CONSTANT_Class_info用于表示类或接口的完全限定名。
 */
public class Constant_Class_info extends Constant_X_info {
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
