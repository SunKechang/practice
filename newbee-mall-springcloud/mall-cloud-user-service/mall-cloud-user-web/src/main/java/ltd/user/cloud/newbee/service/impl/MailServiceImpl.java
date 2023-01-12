package ltd.user.cloud.newbee.service.impl;

import java.io.File;
import java.util.Properties;

import ltd.user.cloud.newbee.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void simpleMail(String toUser) throws Exception{
//        final String username = "lml2272944319@gmail.com";
//        final String password = "hoxhzxluinwhrgzy";
//
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "465");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.socketFactory.port", "465");
//        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//        Session session = Session.getInstance(prop,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("lml2272944319@gmail.com"));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse("2272944319@qq.com")
//            );
//            message.setSubject("Testing Gmail SSL");
//            message.setText("Dear Mail Crawler,"
//                    + "\n\n Please do not spam my email!");
//
//            Transport.send(message);
//
//            System.out.println("Done");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

        //初始化简单邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送者
        message.setFrom(from);
        //邮件接收者
        message.setTo(toUser);
        //邮件标题
        message.setSubject("简单邮件");
        //邮件内容
        message.setText("简单内容");
        //发送邮件
        javaMailSender.send(message);
    }
}
