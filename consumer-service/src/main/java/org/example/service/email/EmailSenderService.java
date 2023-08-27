package org.example.service.email;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}