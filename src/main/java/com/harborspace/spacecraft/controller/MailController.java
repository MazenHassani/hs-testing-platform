package com.harborspace.spacecraft.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.new_mail.MailRequest;
import com.harborspace.spacecraft.new_mail.MailService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping(path = ApiHelper.API_MAIL_PREFIX) 
@RestController
public class MailController {

  // @PostMapping(path = "/send", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  @RequestMapping(
        value = "/send", 
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String sendMail(@Valid @RequestBody MailRequest mailRequest) throws AddressException, MessagingException {
    MailService mailService = MailService.getInstance();
    mailService.send(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getMessage());
    return "Hi, it's done!! ;)";
  }
}