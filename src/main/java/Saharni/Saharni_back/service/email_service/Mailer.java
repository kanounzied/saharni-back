package Saharni.Saharni_back.service.email_service;

import Saharni.Saharni_back.config.EmailConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class Mailer {
    private EmailConfig emailConfig;
    public Mailer(EmailConfig emailConfig){
        this.emailConfig = emailConfig;
    }

    public void sendEmail(String email, String barName, String password){
        //Create mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        //Create mail instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom("SaharniTeam@saharni.com"); //TODO: Look more into it
        mailMessage.setSubject("[ Create your account now! ]");
        mailMessage.setText("Hello " + barName + ",\n" +
                "In order to finish the account creation process, please log in using the following coorninates;\n" +
                "\nEmail: "+email + "\n" +
                "Password: "+password + "\n" +
                "\nPlease feel free to cantact us whenever you encounter a problem. \n" +
                "Best regards,\n" +
                "Saharni Team");
        mailSender.send(mailMessage);
    }
}
