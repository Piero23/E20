package org.unical.enterprise.mailSender.service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.TaggedInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.shared.dto.MailTransferDto;
import org.unical.enterprise.shared.dto.TicketMailDTO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Base64;
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

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("orderMail.html");
            if (inputStream == null) {
                throw new FileNotFoundException("orderMail.html non trovato.");
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
    public void sendQrCodeMail(String to, TicketMailDTO ticketMailDTO) {
        String subject = "🎫 Il tuo biglietto per " + ticketMailDTO.nomeEvento() + " è pronto!";

        try {
            Message message = setupMessage(to);
            message.setSubject(subject);
            Multipart multipart = new MimeMultipart("related");

            // === CARICA E PROCESSA TEMPLATE HTML ===
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ticketMail.html");
            if (inputStream == null) {
                throw new FileNotFoundException("ticketMail.html non trovato in resources/");
            }

            String htmlTemplate;
            try (inputStream) {
                htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            // Sostituisci variabili e escape HTML
            String processedHtml = htmlTemplate
                    .replace("${nome}", ticketMailDTO.nome() != null ?
                            ticketMailDTO.nome().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") : "")
                    .replace("${cognome}", ticketMailDTO.cognome() != null ?
                            ticketMailDTO.cognome().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") : "")
                    .replace("${nomeEvento}", ticketMailDTO.nomeEvento() != null ?
                            ticketMailDTO.nomeEvento().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") : "")
                    .replace("${qrCodeUrl}", "cid:qr-code-inline");

            // === PARTE HTML ===
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(processedHtml, "text/html; charset=utf-8");

            // === QR CODE INLINE ===
            MimeBodyPart qrInlinePart = new MimeBodyPart();
            byte[] qrBytes = Base64.getDecoder().decode(ticketMailDTO.qr());
            qrInlinePart.setDataHandler(new DataHandler(new DataSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(qrBytes);
                }
                @Override
                public OutputStream getOutputStream() throws IOException {
                    throw new IOException("Cannot write to this DataSource");
                }
                @Override
                public String getContentType() { return "image/png"; }
                @Override
                public String getName() { return "qr-code"; }
            }));
            qrInlinePart.setDisposition(MimeBodyPart.INLINE);
            qrInlinePart.setHeader("Content-ID", "<qr-code-inline>");

            // === QR CODE ALLEGATO ===
            MimeBodyPart qrAttachmentPart = new MimeBodyPart();
            qrAttachmentPart.setDataHandler(new DataHandler(new DataSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(qrBytes);
                }
                @Override
                public OutputStream getOutputStream() throws IOException {
                    throw new IOException("Cannot write to this DataSource");
                }
                @Override
                public String getContentType() { return "image/png"; }
                @Override
                public String getName() { return "qr-attachment"; }
            }));
            qrAttachmentPart.setFileName("biglietto-" +
                    ticketMailDTO.nomeEvento().replaceAll("[^a-zA-Z0-9\\-_]", "-") + ".png");
            qrAttachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);

            // === ASSEMBLA EMAIL ===
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(qrInlinePart);
            multipart.addBodyPart(qrAttachmentPart);

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("✅ Email biglietto inviata con successo a: " + to +
                    " per evento: " + ticketMailDTO.nomeEvento());

        } catch (Exception e) {
            System.err.println("❌ Errore durante invio email biglietto a: " + to +
                    " per evento: " + ticketMailDTO.nomeEvento());
            e.printStackTrace();
            throw new RuntimeException("Impossibile inviare email biglietto", e);
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
