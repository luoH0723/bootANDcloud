package com.lhstudy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class SpringbootMailApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() {
        //一个简单的邮件发送
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setSubject("邮件测试");
        mailMessage.setText("这是邮件正文");

        mailMessage.setTo("3247013580@qq.com");
        mailMessage.setFrom("3035419253@qq.com");


        mailSender.send(mailMessage);
    }

    @Test
    void contextLoads2() throws MessagingException {
        //一个复杂的邮件发送
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        //正文
        helper.setSubject("复杂邮件测试");
        helper.setText("<p style='color:red'>这是复杂邮件正文,开启html解析</p>",true);

        //附件
        helper.addAttachment("1.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));
        helper.addAttachment("2.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));

        helper.setTo("3247013580@qq.com");
        helper.setFrom("3035419253@qq.com");

        mailSender.send(mimeMessage);
    }

    /**
     *
     * @param html
     * @param subject  邮件标题
     * @param text  邮件正文
     * @throws MessagingException
     * @Author lh
     */
    public void senMail(Boolean html,String subject,String text) throws MessagingException{
        //一个复杂的邮件发送
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,html);
        //正文
        helper.setSubject(subject);
        helper.setText(text,true);

        //附件
        helper.addAttachment("1.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));
        helper.addAttachment("2.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));

        helper.setTo("3247013580@qq.com");
        helper.setFrom("3035419253@qq.com");

        mailSender.send(mimeMessage);
    }
}
