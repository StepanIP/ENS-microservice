package org.example.service.twilio.impl;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.example.service.configuration.twilio.TwilioConfiguration;
import org.example.service.email.EmailSenderService;
import org.example.service.twilio.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender implements SmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);

    private final TwilioConfiguration twilioConfiguration;

    public TwilioConfiguration getTwilioConfiguration() {
        return twilioConfiguration;
    }

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendMessage(String phoneNumber, String message) {
        LOGGER.info("Sending SMS to {}: {}", phoneNumber, message);

        if (isPhoneNumberValid(phoneNumber)) {
            PhoneNumber to = new PhoneNumber(phoneNumber);
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getPhoneNumber());
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();

            LOGGER.info("SMS sent successfully.");
        } else {
            LOGGER.error("Invalid phone number: {}", phoneNumber);
            throw new IllegalArgumentException("Phone number [" + phoneNumber + "] is not a valid number");
        }
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        phoneNumber = phoneNumber.replaceAll("\\s+", "");
        String regex = "^\\+\\d{1,3}\\d{9}$";
        return phoneNumber.matches(regex);
    }
}
