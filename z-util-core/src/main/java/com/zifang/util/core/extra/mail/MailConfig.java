package com.zifang.util.core.extra.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮件配置类
 */
public class MailConfig {

    private String host;
    private int port = 25;
    private String username;
    private String password;
    private boolean useSsl = false;
    private boolean useTls = false;
    private String from;
    private String fromName;
    private boolean debug = false;

    public MailConfig() {
    }

    public static MailConfig of(String host, String username, String password) {
        MailConfig config = new MailConfig();
        config.host = host;
        config.username = username;
        config.password = password;
        return config;
    }

    public static MailConfig of163(String username, String password) {
        MailConfig config = new MailConfig();
        config.host = "smtp.163.com";
        config.port = 25;
        config.username = username;
        config.password = password;
        config.from = username;
        return config;
    }

    public static MailConfig ofQQ(String username, String password) {
        MailConfig config = new MailConfig();
        config.host = "smtp.qq.com";
        config.port = 465;
        config.username = username;
        config.password = password;
        config.from = username;
        config.useSsl = true;
        return config;
    }

    public static MailConfig ofGmail(String username, String password) {
        MailConfig config = new MailConfig();
        config.host = "smtp.gmail.com";
        config.port = 587;
        config.username = username;
        config.password = password;
        config.from = username;
        config.useTls = true;
        return config;
    }

    public MailConfig host(String host) {
        this.host = host;
        return this;
    }

    public MailConfig port(int port) {
        this.port = port;
        return this;
    }

    public MailConfig username(String username) {
        this.username = username;
        return this;
    }

    public MailConfig password(String password) {
        this.password = password;
        return this;
    }

    public MailConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public MailConfig useTls(boolean useTls) {
        this.useTls = useTls;
        return this;
    }

    public MailConfig from(String from) {
        this.from = from;
        return this;
    }

    public MailConfig fromName(String fromName) {
        this.fromName = fromName;
        return this;
    }

    public MailConfig debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public boolean isUseTls() {
        return useTls;
    }

    public String getFrom() {
        return from != null ? from : username;
    }

    public String getFromName() {
        return fromName;
    }

    public boolean isDebug() {
        return debug;
    }

    public Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }

    public java.util.Properties toProperties() {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");

        if (useSsl) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", String.valueOf(port));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        if (useTls) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        if (debug) {
            props.put("mail.debug", "true");
        }

        return props;
    }
}