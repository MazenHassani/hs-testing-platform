package com.harborspace.spacecraft.new_mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {
    private static MailService mailService = null;
    private Properties prop;
    private Session session;

    private void setup() {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", Constants.host);
        prop.put("mail.smtp.port", Constants.port);
        prop.put("mail.smtp.ssl.trust", Constants.host);

    }

    public static MailService getInstance() {
        if (mailService == null) {
            mailService = new MailService();
            return mailService;

        } else
            return MailService.mailService;
    }

    private MailService() {
        setup();
        connect();
    }

    private void connect() {
       
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.username, Constants.password);
            }
        });
    }

    public void send(String emailTo, String subject, String msg) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Constants.from));
        message.setRecipients(
        Message.RecipientType.TO, InternetAddress.parse(emailTo));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
