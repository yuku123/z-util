package com.zifang.util.xml.model;

/**
 * XML CDATA 节。
 *
 * @author zifang
 */
public class XCData implements XNode {

    private final String data;

    public XCData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }
}
