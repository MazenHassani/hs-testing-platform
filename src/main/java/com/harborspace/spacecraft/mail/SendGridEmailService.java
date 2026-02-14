package com.harborspace.spacecraft.mail;

import com.harborspace.spacecraft.invitation.model.Invitation;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendGridEmailService implements EmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridAPIKey;

    @Value("${sendgrid.templateId}")
    private String EMAIL_TEMPLATE_ID;

    private SendGrid sendGridClient;

    @Override
    public boolean sendText(MailRequest request) {
        if (sendGridClient == null) {
            sendGridClient = new SendGrid(sendGridAPIKey);
        }
        return sendEmail(request.from, request.to, request.getSubject(), new Content("text/plain", request.getMessage()));
    }

    @Override
    public boolean sendHtml(MailRequest request) {
        if (sendGridClient == null) {
            sendGridClient = new SendGrid(sendGridAPIKey);
        }
        return sendEmail(request.from, request.to, request.getSubject(), new Content("text/html", request.getMessage()));
    }

    private boolean sendEmail(String from, String to, String subject, Content content) {
        Mail mail = new Mail(new Email(from), subject, new Email(to), content);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            this.sendGridClient.api(request);
            return true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return false;
        }

    }

    public boolean sendEmailWithSendGrid(MailRequest mailRequest, String courseName, Invitation.InvitationType type, String URL) {
        if (sendGridClient == null) {
            sendGridClient = new SendGrid(sendGridAPIKey);
        }

        Mail mail = new Mail(new Email(mailRequest.from), mailRequest.subject, new Email(mailRequest.to), new Content("text/html", mailRequest.getMessage()));
        String pretty_type = type == Invitation.InvitationType.STUDENT ? "student" : "teacher";
        mail.personalization.get(0).addDynamicTemplateData("stars_invitation",URL);
        mail.personalization.get(0).addDynamicTemplateData("stars_role", pretty_type);
        mail.personalization.get(0).addDynamicTemplateData("stars_course",courseName);
        mail.setTemplateId(EMAIL_TEMPLATE_ID);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            this.sendGridClient.api(request);
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }
}
