package com.zifang.util.proxy.a.decompile.bean.attribute;

/**
 * 已弃用属性类
 * <p>
 * Deprecated_attribute用于标记类、接口、字段或方法已被弃用。
 */
public class Attribute_Deprecated_info extends Attribute_info {
    private int attribute_name_index;
    private int attribute_length;

    @Override
    public int getAttribute_name_index() {
        return attribute_name_index;
    }

    @Override
    public void setAttribute_name_index(int attribute_name_index) {
        this.attribute_name_index = attribute_name_index;
    }

    public int getAttribute_length() {
        return attribute_length;
    }

    public void setAttribute_length(int attribute_length) {
        this.attribute_length = attribute_length;
    }


}
