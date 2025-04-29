package com.example.wizquiz;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OTPEmailSender {
    //parametrat SMTP
    private static final String smtpHost = "smtp.gmail.com";
    private static final String smtpPort = "587";
    private static final String senderEmail = "lifecola.company@gmail.com";
    private static final String senderPassword = "juda zpsp pkxj nhqy";

    public static boolean sendEmail(String recipient, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

//        behet ni session

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
//            pergatit MimeMessage: from, to, subject
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);
            Transport.send(message); // e dergon emailin
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
