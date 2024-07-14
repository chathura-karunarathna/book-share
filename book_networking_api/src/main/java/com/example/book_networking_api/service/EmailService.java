package com.example.book_networking_api.service;

import com.example.book_networking_api.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplate,
                          String confirmationUrl,
                          String activationCode,
                          String subject ) throws MessagingException;
}
