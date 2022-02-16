package com.jiahe.iot.common.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {
    //    cloud@jiahe2smart.com
    private static String emailAcount = "cloud@jiahe2smart.com";
    private static String emailServer = "smtp.exmail.qq.com";
    private static String code = "seEYJX7QfVk4azEj";

//    private static String emailAcount = "924857883@qq.com";
//    private static String emailServer = "smtp.qq.com";
//    private static String code = "hwgbpkfymwgkbbjf";


    public static boolean sendEmail(String emailAddr, String content, String topic) {
        try {
            Properties pro = new Properties();
            // 需要指定邮件的服务器地址，复制一下。推荐去复制，自己写容易写错了。
            //以qq邮箱为例子
            // 邮件服务器主机
            pro.setProperty("mail.host", emailServer);
            // 邮件传输协议
            pro.setProperty("mail.transport.protocol", "smtp");
            // 设置邮件发送需要认证
            pro.put("mail.smtp.auth", "true");
            // 连接邮件的服务器，会话
            Session session = Session.getDefaultInstance(pro);
            // 获取到传输对象
            Transport transport = session.getTransport();
            // 校验账号和密码（该密码不是QQ号的密码），授权码
            // 是固定，必须传入授权码
            //在这里两个参数，第一个参数是你的邮箱，第二个参数是授权码
            transport.connect(emailAcount, code);

            // =============================================
            // 2. 构建出一封邮件（设置收件人、设置主题和设置正文）
            // 有邮件的类
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            //这里的参数是发件人邮箱
            message.setFrom(new InternetAddress(emailAcount));

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddr));
            // 设置主题
            message.setSubject(topic);
            // 设置正文
            message.setContent(content, "text/html;charset=UTF-8");

            // 3. 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            // 关闭连接
            transport.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        sendEmail("924857883@qq.com", "测试", "测试");
    }
}
