package com.zifang.util.xml.model;

/**
 * XML 处理指令。
 *
 * @author zifang
 */
public class XProcessingInstruction implements XNode {

    private final String target;

    private final String data;

    public XProcessingInstruction(String target, String data) {
        this.target = target;
        this.data = data;
    }

    public String getTarget() {
        return target;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "<?" + target + " " + data + "?>";
    }
}
