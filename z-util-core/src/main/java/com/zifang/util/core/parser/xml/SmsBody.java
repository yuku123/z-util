package com.zifang.util.core.parser.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * 请求内容
 *
 * @author xuejiangtao
 * Jul 25, 2013 9:34:16 PM
 */
public class SmsBody {

    /**
     * 短信内容
     */
    private String content;

    /**
     * 手机号
     */
    private String sourceAddr;

    /**
     * 服务代码
     */
    private String destAddr;

    public String getContent() {
        return content;
    }

    @XmlElement(name = "CONTENT")
    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    @XmlElement(name = "SOURCEADDR")
    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public String getDestAddr() {
        return destAddr;
    }

    @XmlElement(name = "DESTADDR")
    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    @Override
    public String toString() {
        return "SmsBody{content=" + content + ", sourceAddr=" + sourceAddr + ", destAddr=" + destAddr + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsBody smsBody = (SmsBody) o;
        return java.util.Objects.equals(content, smsBody.content) &&
                java.util.Objects.equals(sourceAddr, smsBody.sourceAddr) &&
                java.util.Objects.equals(destAddr, smsBody.destAddr);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(content, sourceAddr, destAddr);
    }

}