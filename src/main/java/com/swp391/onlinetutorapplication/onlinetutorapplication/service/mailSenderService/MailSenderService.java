package com.swp391.onlinetutorapplication.onlinetutorapplication.service.mailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderService {
    public String htmlEmailForToken(String token, String username) {
        return "<html>\n" +
                "<body>\n" +
                "\t<h2>Email confirmation account</h2>\n" +
                "\t<p>Dear " + username + " ,</p>\n" +
                "\t<p>This is email for confirm your registration account. Please click the link below to confirm your registration.</p><br>\n" +
                "\t<a href=\"http://localhost:8080/api/auth/activate?token=" + token + "\"><button>CLICK HERE</button></a>\n" +
                "\t<br>\n" +
                "\t<p>Thanks for using our product. Have a nice day!</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public String htmlEmailForForgetPassword(Long resetCode, String username) {
        return "<html>\n" +
                "<body>\n" +
                "\t<h2>Email Reset Password</h2>\n" +
                "\t<p>Dear " + username + " ,</p>\n" +
                "\t<p>This is email for confirm your registration account. Please use code below to reset your password.</p><br>\n" +
                "\t<a href=\"http://localhost:8080/api/auth/reset-password/" + resetCode + "\"><button>CLICK HERE</button></a>\n" +
                "\t<br>\n" +
                "\t<p>Thanks for using our product. Have a nice day!</p>\n" +
                "</body>\n" +
                "</html>";
    }


    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailActivate(String username, String token, String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String htmlMessage = htmlEmailForToken(token, username);

        message.setContent(htmlMessage, "text/html");

        helper.setTo(email);
        helper.setSubject("CONFIRM ACCOUNT REGISTRATION");
        javaMailSender.send(message);
    }

    public void sendEmailResetPassword(String username, Long resetCode, String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMessage = htmlEmailForForgetPassword(resetCode, username);

        message.setContent(htmlMessage, "text/html");
        helper.setTo(email);
        helper.setSubject("RESET PASSWORD");
        javaMailSender.send(message);
    }
}
