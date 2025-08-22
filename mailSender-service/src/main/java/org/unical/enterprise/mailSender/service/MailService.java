package org.unical.enterprise.mailSender.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.shared.dto.MailTransferDto;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    final Properties props = setupProperties();

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    @Transactional
    public void sendMail(MailTransferDto ordine){
        String subject = "Acquisto Confermato";

        try {
            Message message = setupMessage(ordine.mail());
            message.setSubject(subject);

            Instant instant = ordine.data().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            LocalDate data = instant.atZone(zoneId).toLocalDate();
            LocalTime ora = instant.atZone(zoneId).toLocalTime();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("index.html");
            if (inputStream == null) {
                throw new FileNotFoundException("index.html non trovato nel classpath.");
            }
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);


            String htmlContent = htmlTemplate
                    .replace("${username}", ordine.cliente())
                    .replace("${ordineId}", ordine.ID().toString())
                    .replace("${dataOrdine}", data.toString())
                    .replace("${oraOrdine}", ora.toString())
                    .replace("${importo}", String.format("%.2f", ordine.importo()));


            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(htmlContent, "text/html; charset=utf-8");

            multipart.addBodyPart(textPart);

            message.setContent(multipart);

            Transport.send(message);
            //System.out.println("Email inviata con successo!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void sendQrCodeMail(String to /*, String image*/) {

        String subject = "Ecco Il Tuo Biglietto!";

        try {
            Message message = setupMessage(to);
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            String body = """
            <html>
                <body>
                    <h2>Grazie per il tuo acquisto!</h2>
                    <p>In allegato trovi il tuo biglietto con QR Code. Mostralo all'ingresso.</p>
                    <p>Buon divertimento!</p>
                    <br>
                </body>
            </html>
            """;
            textPart.setContent(body, "text/html; charset=utf-8");

            /*
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

             */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Properties setupProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Message setupMessage(String to) throws Exception {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, "Esse20"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        return message;
    }
}
