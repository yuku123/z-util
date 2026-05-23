package com.zifang.util.xml.model;

/**
 * XML 文档声明。
 *
 * @author zifang
 */
public class XDeclaration {

    private String version = "1.0";

    private String encoding;

    private String standalone;

    public XDeclaration() {
    }

    public XDeclaration(String version, String encoding, String standalone) {
        this.version = version;
        this.encoding = encoding;
        this.standalone = standalone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getStandalone() {
        return standalone;
    }

    public void setStandalone(String standalone) {
        this.standalone = standalone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<?xml version=\"" + version + "\"");
        if (encoding != null) {
            sb.append(" encoding=\"").append(encoding).append("\"");
        }
        if (standalone != null) {
            sb.append(" standalone=\"").append(standalone).append("\"");
        }
        sb.append("?>");
        return sb.toString();
    }
}
