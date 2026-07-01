package be.kdg.integration.gameapplication.model.authservices.services;

import be.kdg.integration.gameapplication.model.credentials.CredentialsManager;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

/**
 * Handles email delivery for password recovery using Gmail SMTP.
 * <p>
 * Connects to Gmail's SMTP server via STARTTLS on port 587.
 * Credentials are loaded from {@link CredentialsManager}.
 * </p>
 *
 * @see CredentialsManager
 */

public class MailService{
    private Session session;
    private Properties props;
    private String mailFrom;
    private String passwd;

    /**
     * Creates a new {@link MailService} and initializes the SMTP session.
     */
    public MailService(){
        initializeUser();
        initializeProperties();
        initializeSession();
    }

    private void initializeUser(){
        this.mailFrom = CredentialsManager.SMTP_USER;
        this.passwd = CredentialsManager.SMTP_PASSWORD;
    }

    private void initializeSession(){
        this.session = Session.getInstance(props, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(mailFrom, passwd);
            }
        });
    }

    private void initializeProperties(){
        this.props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    private String generateCode(){
        int codeLength = 5;
        SecureRandom rand = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for(int i = 0; i < codeLength; i++){
            char c = (char) rand.nextInt(65, 90);
            code.append(c);
        }
        return code.toString();
    }

    /**
     * Sends a randomly generated code to provided email address
     * <p>
     * The code contains 5 letters in uppercase
     * </p>
     *
     * @param mail the recipient email address
     * @return string of generated and sent to the user code
     * @throws MessagingException if we cannot reach game email address, players mail address, formatting exception occur or code was not delivered
     */

    public String sendForgetPasswordRequest(String mail) throws MessagingException{
        Message message = new MimeMessage(this.session);

        try{
            message.setFrom(new InternetAddress(mailFrom));
        }catch (MessagingException e) {
            throw new MessagingException("Cannot reach game mail address", e);
        }
        try{
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
        }catch (MessagingException e) {
            throw new MessageRemovedException("Cannot reach the player mail address", e);
        }

        String code = generateCode();

        try{
            message.setSubject("Request for changing password");
            message.setText("Here is your generated code: " + code);
        }catch (MessagingException e) {
            throw new MessagingException("Something went wrong during formatting message", e);
        }

        try{
            Transport.send(message);
        }catch (MessagingException e) {
            throw new MessagingException("Something went wrong during transporting the message", e);
        }

        return code;
    }
}
