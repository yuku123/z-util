package com.zifang.util.proxy.a.decompile.bean;

/**
 * 字段名与类型组合类
 * <p>
 * 用于存储字段的名称和类型描述符。
 */
public class FieldNameAndType {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
