package demacs.unical.esse20.service;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class MailService {

    public void sendMail(String to, String subject, String body, String image) {

        final String username = "noreply.esse20@gmail.com";
        final String password = "lqvxdvovovxwmjfd";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html; charset=utf-8");

            MimeBodyPart imgPart = new MimeBodyPart();
            DataHandler dh = new DataHandler(Base64.getDecoder().decode(image), "image/png");
            imgPart.setDataHandler(dh);
            imgPart.setDisposition( MimeBodyPart.INLINE );
            imgPart.addHeader("Content-ID", "<image>");

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imgPart);

            message.setContent(multipart);

            Transport.send(message);
            //System.out.println("Email inviata con successo!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
