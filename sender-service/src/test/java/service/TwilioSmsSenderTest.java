package service;

import com.twilio.rest.api.v2010.account.MessageCreator;
import jakarta.mail.internet.MimeMessage;
import org.example.SenderServiceApplication;
import org.example.configuration.twilio.TwilioConfiguration;
import org.example.service.twilio.impl.TwilioSmsSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SenderServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class TwilioSmsSenderTest {

    private static final String VALID_PHONE_NUMBER = "+380681918275";
    private static final String INVALID_PHONE_NUMBER = "invalid_number";
    private static final String MESSAGE = "Test message";

    @Mock
    private TwilioConfiguration twilioConfiguration;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MessageCreator messageCreator;

    @InjectMocks
    private TwilioSmsSender twilioSmsSender;

    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsSender.class);

    @Test
    public void testSendMessage_ValidPhoneNumber_Successful() {
        when(twilioSmsSender.getTwilioConfiguration().getPhoneNumber()).thenReturn("+12344054366");
        TwilioSmsSender spySmsSender = spy(twilioSmsSender);

        //To not spend my free trial money on tests :)
        doNothing().when(spySmsSender).sendMessage(anyString(), anyString());

        spySmsSender.sendMessage(VALID_PHONE_NUMBER, MESSAGE);

        verify(mailSender, never()).send(any(MimeMessage.class));
        verify(spySmsSender, times(1)).sendMessage(VALID_PHONE_NUMBER, MESSAGE);
    }

    @Test
    public void testSendMessage_InvalidPhoneNumber_ExceptionThrown() {
        TwilioSmsSender spySmsSender = spy(twilioSmsSender);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            spySmsSender.sendMessage(INVALID_PHONE_NUMBER, MESSAGE);
        });

        String expectedMessage = "Phone number [" + INVALID_PHONE_NUMBER + "] is not a valid number";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testIsPhoneNumberValid_ValidPhoneNumbers_ReturnsTrue() {
        assertTrue(twilioSmsSender.isPhoneNumberValid("+380681918275"));
        assertTrue(twilioSmsSender.isPhoneNumberValid("+123456789012"));
    }

    @Test
    public void testIsPhoneNumberValid_InvalidPhoneNumbers_ReturnsFalse() {
        assertFalse(twilioSmsSender.isPhoneNumberValid("invalid_number"));
        assertFalse(twilioSmsSender.isPhoneNumberValid("12345"));
        assertFalse(twilioSmsSender.isPhoneNumberValid(""));
        assertFalse(twilioSmsSender.isPhoneNumberValid(null));
    }
}
