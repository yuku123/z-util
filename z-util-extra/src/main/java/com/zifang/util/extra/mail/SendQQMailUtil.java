package com.zifang.util.extra.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * QQ邮箱邮件发送工具类
 * <p>
 * 提供基于QQ邮箱SMTP服务的邮件发送功能，支持SSL安全连接
 *
 * @author zifang
 */
public class SendQQMailUtil {

    /**
     * 发送测试邮件（仅用于测试QQ邮箱SMTP配置）
     * <p>
     * 该方法演示如何使用QQ邮箱SMTP服务发送邮件
     *
     * @param args 命令行参数（未使用）
     * @throws MessagingException 如果邮件配置或发送失败则抛出此异常
     */
    public static void main(String[] args) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress("1340947819@qq.com"));
        // 设置收件人邮箱地址
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("1340947819@qq.com"), new InternetAddress("1340947819@qq.com"), new InternetAddress("xxx@qq.com")});
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人
        // 设置邮件标题
        message.setSubject("xmqtest");
        // 设置邮件内容
        message.setText("邮件内容邮件内容邮件内容xmqtest");
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("zhangchenhao1995@163.com", "zxczxc123");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}