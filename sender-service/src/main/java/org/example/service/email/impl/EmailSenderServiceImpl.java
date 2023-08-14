package org.example.service.email.impl;

import org.example.service.configuration.email.MailConfiguration;
import org.example.service.email.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    private final JavaMailSender mailSender;
    private final MailConfiguration mailConfiguration;

    public EmailSenderServiceImpl(JavaMailSender mailSender, MailConfiguration mailConfiguration) {
        this.mailSender = mailSender;
        this.mailConfiguration = mailConfiguration;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public MailConfiguration getMailConfiguration() {
        return mailConfiguration;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        LOGGER.info("Sending username to: {}, subject: {}", to, subject);

        if (mailConfiguration.getUsername() == null) {
            LOGGER.error("Error, Sender cannot be null");
            throw new NullPointerException("Error, Sender cannot be null");
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailConfiguration.getUsername());
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        this.mailSender.send(simpleMailMessage);

        LOGGER.info("Email sent successfully.");
    }
}
