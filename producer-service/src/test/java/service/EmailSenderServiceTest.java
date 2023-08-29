package service;

import org.example.ProducerServiceApplication;
import org.example.configuration.email.MailConfiguration;
import org.example.service.email.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProducerServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender mockMailSender;
    @Mock
    private MailConfiguration mockMailConfiguration;
    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;

    @Test
    public void testSendEmail() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";
        String sender = "sender@example.com";

        when(emailSenderService.getMailConfiguration().getUsername()).thenReturn(sender);

        emailSenderService.sendEmail(to, subject, message);

        verify(emailSenderService.getMailSender(), times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailWithCorrectValues() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";
        String sender = "sender@example.com";

        when(emailSenderService.getMailConfiguration().getUsername()).thenReturn(sender);

        emailSenderService.sendEmail(to, subject, message);

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSenderService.getMailSender()).send(argumentCaptor.capture());
        SimpleMailMessage sentMailMessage = argumentCaptor.getValue();

        assertEquals(sender, sentMailMessage.getFrom());
        assertEquals(to, Objects.requireNonNull(sentMailMessage.getTo())[0]);
        assertEquals(subject, sentMailMessage.getSubject());
        assertEquals(message, sentMailMessage.getText());
    }

    @Test
    public void testSendEmailWithNullSender() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        when(emailSenderService.getMailConfiguration().getUsername()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> emailSenderService.sendEmail(to, subject, message));
    }
}

