package com.playdata.HumanResourceManagement.company.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender emailSender;

    public void sendRegistrationInfo(String name, String email, String companyCode, String employeeId) throws MessagingException {
        String htmlContent = "<html>" +
                "<body>" +
                "<h1 style='color: orange;'>회원가입 완료</h1>" +
                "<p>회원가입이 완료되었습니다!</p>" +
                "<p>"+name +"님"+
                "<p><b>회사 코드:</b> " + companyCode + "</p>" +
                "<p><b>아이디:</b> " + employeeId + "</p>" +
                "</body>" +
                "</html>";

        sendEmail(email, "[InsaHI] 회원가입 완료", htmlContent);
    }

    private void sendEmail(String email, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }
}
