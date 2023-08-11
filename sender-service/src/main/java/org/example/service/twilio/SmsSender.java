package org.example.service.twilio;

public interface SmsSender {

    void sendMessage(String phoneNumber, String message);
}