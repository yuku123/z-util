package com.zifang.util.xml.model;

/**
 * XML 文本节点。
 *
 * @author zifang
 */
public class XText implements XNode {

    private String text;

    public XText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
