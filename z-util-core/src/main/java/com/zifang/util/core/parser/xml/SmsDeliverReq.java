package com.zifang.util.core.parser.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SMS 短信下发请求XML结构。
 * <p>
 * 对应 SMSDELIVERREQ 根节点，包含请求头和短信内容列表。
 *
 * @author zifang
 * @see ReqHeader
 * @see SmsBody
 */
@XmlRootElement(name = "SMSDELIVERREQ")
public class SmsDeliverReq {

    private ReqHeader reqHeader;

    private List<SmsBody> smsBodys;

    @XmlElement(name = "REQHEADER")
    public ReqHeader getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(ReqHeader reqHeader) {
        this.reqHeader = reqHeader;
    }

    @XmlElementWrapper(name = "SMSBODYS")
    @XmlElement(name = "SMSBODY")
    public List<SmsBody> getSmsBodys() {
        return smsBodys;
    }

    public void setSmsBodys(List<SmsBody> smsBodys) {
        this.smsBodys = smsBodys;
    }
}