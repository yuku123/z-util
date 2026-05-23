package com.zifang.util.xml.model;

/**
 * XML 注释节点。
 *
 * @author zifang
 */
public class XComment implements XNode {

    private final String content;

    public XComment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "<!--" + content + "-->";
    }
}
