//package ltd.user.cloud.newbee.service.impl;
//
//import java.io.File;
//import ltd.user.cloud.newbee.service.MailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class MailServiceImpl implements MailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.username}")
//    private String from;
//
//    @Override
//    public void simpleMail(String toUser) throws Exception{
//        //初始化简单邮件对象
//        SimpleMailMessage message = new SimpleMailMessage();
//        //邮件发送者
//        message.setFrom(from);
//        //邮件接收者
//        message.setTo(toUser);
//        //邮件标题
//        message.setSubject("简单邮件");
//        //邮件内容
//        message.setText("简单内容");
//        //发送邮件
//        javaMailSender.send(message);
//    }
//}
