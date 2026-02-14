package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.mail.MailRequest;
import com.harborspace.spacecraft.mail.SendGridEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendGridEmailServiceTest {
    private final static String fromMail = "test@example.com";
    private final static String toMail = "afevgenij@mail.ru";
    
    @Autowired
    private SendGridEmailService emailService;

    @Test
    public void sendText() {
        assertTrue(emailService.sendText(MailRequest.builder()
                .from(fromMail)
                .to(toMail)
                .subject("Hello")
                .message("Hello World")
                .build()));
    }

    @Test
    public void sendHtml() {
        assertTrue(emailService.sendHtml(MailRequest.builder()
                .from(fromMail)
                .to(toMail)
                .subject("Hello")
                .message("<b>Hello</b>,<br/> \\\n" +
                        "You are invited to the class: %s<br/> \\\n" +
                        "<a href=\"%s\">link to join</a>")
                .build()));
    }
}
