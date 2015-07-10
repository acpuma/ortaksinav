package net.yazsoft.frame.mail;


import javax.inject.Inject;
import javax.inject.Named;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Named
@ViewScoped
public class Email {
    private SimpleMailMessage templateMessage;

    @Inject
    MailSender mailSender;

    public void sendMail(String to, String subject, String body)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("info@ortaksinav.com.tr");  // dont send without from address
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            Util.setFacesMessage("EMAIL SENT");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTestMail() {
        sendMail("cumacakmak@gmail.com","java mail", "TESTTTT");
    }
}
