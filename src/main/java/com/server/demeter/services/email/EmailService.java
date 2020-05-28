package com.server.demeter.services.email;

import javax.mail.internet.MimeMessage;

import com.server.demeter.domain.User;
import com.server.demeter.domain.VerificationToken;

public interface EmailService {

    void sendHtmlEmail(MimeMessage msg);
    void sendConfirmationHtmlEmail(User user, VerificationToken vToken, int select);
    
}