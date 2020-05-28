package com.server.demeter.services.email;

import java.sql.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.server.demeter.domain.User;
import com.server.demeter.domain.VerificationToken;
import com.server.demeter.services.UserService;
import com.server.demeter.services.exception.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Value("${default.url}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;

    @Override
    public void sendHtmlEmail(MimeMessage msg) {

    }

    @Override
    public void sendConfirmationHtmlEmail(User user, VerificationToken vToken, int select) {
        try {
            MimeMessage mime = prepareMimeMessageFromUser(user, vToken, select);
            sendHtmlEmail(mime);
        } catch (MessagingException msg) {
            throw new ObjectNotFoundException(String.format("rro ao tentar enviar o e-mail"));
        }
    }

    protected MimeMessage prepareMimeMessageFromUser(User user, VerificationToken vToken, int select)
            throws MessagingException {
        MimeMessage mimemessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimehelper = new MimeMessageHelper(mimemessage, true);

        mimehelper.setTo(user.getEmail());
        mimehelper.setFrom(this.sender);
        mimehelper.setSubject((select==1 ? "Reset de Senha de Usuario" : "Confirmação de Registro"));
        mimehelper.setSentDate(new Date(System.currentTimeMillis()));
        mimehelper.setText(htmlFromTemplateUser(user, vToken, select), true);

        return mimemessage;
    }

    protected String htmlFromTemplateUser(User user, VerificationToken vToken, int select) {
        String token = UUID.randomUUID().toString();

        if (vToken == null) {
            this.userService.createVerificationTokenForUser(user, token);
        } else {
            token = vToken.getToken();
        }

        String confirmationurl = this.contextPath + "/api/public/regitrationConfirm/users?token=" + token;
        
        if (select == 1) {
            confirmationurl = this.contextPath +"/api/public/changepassword/users?id=" + user.getId() + "&token=" + token;
        }

        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("confirmationUrl", confirmationurl);
 
        return templateEngine.process("email/registerUser", context);
    }
}