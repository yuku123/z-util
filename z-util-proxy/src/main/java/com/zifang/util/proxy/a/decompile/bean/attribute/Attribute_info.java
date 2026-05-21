package com.zifang.util.proxy.a.decompile.bean.attribute;

/**
 * 属性信息基类
 * <p>
 * 所有属性的父类，包含属性名索引、属性长度和信息。
 */
public class Attribute_info {

    private int attribute_name_index;
    private int attrbute_length;
    private String info;

    private String attribute_type;

    public int getAttribute_name_index() {
        return attribute_name_index;
    }

    public void setAttribute_name_index(int attribute_name_index) {
        this.attribute_name_index = attribute_name_index;
    }

    public int getAttrbute_length() {
        return attrbute_length;
    }

    public void setAttrbute_length(int attrbute_length) {
        this.attrbute_length = attrbute_length;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAttribute_type() {
        return attribute_type;
    }

    public void setAttribute_type(String attribute_type) {
        this.attribute_type = attribute_type;
    }


}
