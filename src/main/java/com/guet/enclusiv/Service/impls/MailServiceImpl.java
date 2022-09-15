package com.guet.enclusiv.Service.impls;

import com.guet.enclusiv.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean send(String email, String messageString) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("校验码");
        message.setFrom("437225121@qq.com");
        message.setTo(email);
        message.setSentDate(new Date());
        message.setText(messageString);
        javaMailSender.send(message);
        return false;
    }

}
