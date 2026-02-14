package com.harborspace.spacecraft.mail;

public interface EmailService {
    boolean sendText(MailRequest request);
    boolean sendHtml(MailRequest request);
}
