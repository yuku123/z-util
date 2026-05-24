package com.zifang.util.extra;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件消息类
 */
public class Mail {

    private String subject;
    private String from;
    private String fromName;
    private List<String> to = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();
    private String content;
    private boolean html = false;
    private List<File> attachments = new ArrayList<>();

    public Mail() {
    }

    public static Mail create() {
        return new Mail();
    }

    public Mail subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail from(String from) {
        this.from = from;
        return this;
    }

    public Mail fromName(String fromName) {
        this.fromName = fromName;
        return this;
    }

    public Mail to(String... to) {
        for (String t : to) {
            this.to.add(t);
        }
        return this;
    }

    public Mail to(List<String> to) {
        this.to.addAll(to);
        return this;
    }

    public Mail cc(String... cc) {
        for (String c : cc) {
            this.cc.add(c);
        }
        return this;
    }

    public Mail cc(List<String> cc) {
        this.cc.addAll(cc);
        return this;
    }

    public Mail bcc(String... bcc) {
        for (String b : bcc) {
            this.bcc.add(b);
        }
        return this;
    }

    public Mail bcc(List<String> bcc) {
        this.bcc.addAll(bcc);
        return this;
    }

    public Mail content(String content) {
        this.content = content;
        return this;
    }

    public Mail html(String htmlContent) {
        this.content = htmlContent;
        this.html = true;
        return this;
    }

    public Mail attach(File... attachments) {
        for (File a : attachments) {
            this.attachments.add(a);
        }
        return this;
    }

    public Mail attach(List<File> attachments) {
        this.attachments.addAll(attachments);
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public String getFrom() {
        return from;
    }

    public String getFromName() {
        return fromName;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getContent() {
        return content;
    }

    public boolean isHtml() {
        return html;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void validate() {
        if (to.isEmpty() && cc.isEmpty() && bcc.isEmpty()) {
            throw new IllegalStateException("At least one recipient (To/CC/BCC) is required");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalStateException("Subject is required");
        }
    }

    /**
     * 发送邮件的便捷方法
     */
    public void send(MailConfig config) {
        SendMailUtil.send(config, this);
    }
}