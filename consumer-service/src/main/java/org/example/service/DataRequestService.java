package org.example.service;

import com.example.common.request.SendRequest;
import org.example.service.email.EmailSenderService;
import org.example.service.twilio.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRequestService.class);

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private EmailSenderService emailSender;

    public void sendDataRequest(SendRequest sendRequest) {
        LOGGER.info("Sending data request to contacts: {}", sendRequest.getContact());
            if (Character.isDigit(sendRequest.getContact().charAt(sendRequest.getContact().length() - 1))) {
                LOGGER.info("Sending SMS to contact: {}", sendRequest.getContact());
                smsSender.sendMessage(sendRequest.getContact(), sendRequest.getMessage());
            } else {
                LOGGER.info("Sending username to contact: {}", sendRequest.getContact());
                emailSender.sendEmail(sendRequest.getContact(), "This is important", sendRequest.getMessage());
            }
        LOGGER.info("Data request sent successfully to all contacts.");
        }
}
