package net.yazsoft.frame.mail;


import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.util.EncodingUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.Java;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class EmailAttach {
    private static final Logger logger = Logger.getLogger(EmailAttach.class);
    private SimpleMailMessage templateMessage;
    String mailFolder="temp";

    @Inject JavaMailSenderImpl mailSender;

    List<String> mailFiles;

    public void init() {
        mailFiles=new ArrayList<>();
    }

    public void sendMailAtachment(String to,String subject, String body, String[] bcc){
        try {
            /*
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost("webmail.ortaksinav.com.tr");
            sender.setPort(587);
            sender.setUsername("info@ortaksinav.com.tr");
            sender.setPassword("sin21");
            */


            MimeMessage message = mailSender.createMimeMessage();

            // use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);
            helper.setFrom("info@ortaksinav.com.tr");  // dont send without from address
            helper.setTo(to);
            helper.setBcc(bcc);
            helper.setSubject(subject);
            helper.setText(body);

            // let's attach the infamous windows Sample file (this time copied to c:/)

            for (String mailFile:mailFiles) {
                FileSystemResource filesr = new FileSystemResource(Util.getUploadsFolder()+"/"+mailFolder+ "/" + mailFile);
                logger.info("LOG02550: FILE : " + filesr.toString());
                //System.setProperty("mail.mime.decodetext.strict","true"); //for filename encoding problem
                helper.addAttachment(Util.changeTurtoEng(mailFile), filesr);
                //helper.addAttachment(MimeUtility.encodeText(mailFile,"UTF-8",null), filesr);
            }
            mailSender.setHost("webmail.ortaksinav.com.tr");
            mailSender.send(message);
        } catch (Exception e) {
            Util.catchException(e);
        }

    }
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
            Util.catchException(e);
        }
    }




    public void sendTestMail() {
        sendMail("cumacakmak@gmail.com","java mail", "TESTTTT");
    }

    public List<String> getMailFiles() {
        return mailFiles;
    }

    public void setMailFiles(List<String> mailFiles) {
        this.mailFiles = mailFiles;
    }

    public String getMailFolder() {
        return mailFolder;
    }

    public void setMailFolder(String mailFolder) {
        this.mailFolder = mailFolder;
    }
}
