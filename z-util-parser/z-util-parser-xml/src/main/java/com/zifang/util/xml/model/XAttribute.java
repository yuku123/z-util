package com.zifang.util.xml.model;

/**
 * XML 属性节点。
 *
 * @author zifang
 */
public class XAttribute {

    private final String name;

    private String value;

    public XAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=\"" + value + "\"";
    }
}
