package org.unical.enterprise.gestioneOrdini.service;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;

import java.util.Base64;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class MailService {

    final String username = "noreply.esse20@gmail.com";
    final String password = "lqvxdvovovxwmjfd";

    final Properties props = setupProperties();

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    @Transactional
    public void sendMail(String to, Ordine ordine){
        String subject = "Acquisto Confermato";

        try {
            Message message = setupMessage(to);
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            String body = String.format("""
            <html>
                <body>
                    <h3>Conferma Ordine Numero %s</h3>
                    <p>Data dell'ordine: %s</p>
                    <br>
                    <p>Dati Di Pagamento</p>
                    <p>Importo: %.2f</p>
                    <p>inserire qui i dati dell'utente<p>
                    <p>Grazie per l'acquisto.</p>
                    <br>
                </body>
            </html>
            """, ordine.getId().toString(), ordine.getData_pagamento().toString(), ordine.getImporto());
            textPart.setContent(body, "text/html; charset=utf-8");

            multipart.addBodyPart(textPart);

            message.setContent(multipart);

            Transport.send(message);
            //System.out.println("Email inviata con successo!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void sendQrCodeMail(String to, String image) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Properties setupProperties(){
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
