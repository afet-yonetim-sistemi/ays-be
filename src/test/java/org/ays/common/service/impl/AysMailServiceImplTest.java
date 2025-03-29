package org.ays.common.service.impl;

import ch.qos.logback.classic.Level;
import jakarta.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.AysMail;
import org.ays.common.model.AysMailBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class AysMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysMailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;


    @Test
    void givenValidEmailAddresses_whenMailSendingSuccessfully_thenLogTraceAboutMailSentSuccessfully() {
        // Given
        AysMail mockMail = new AysMailBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(mailSender.createMimeMessage())
                .thenReturn(Mockito.mock(MimeMessage.class));

        Mockito.doNothing()
                .when(mailSender)
                .send(Mockito.any(MimeMessage.class));

        // Then
        mailService.send(mockMail);

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    Mockito.verify(mailSender, Mockito.times(1))
                            .createMimeMessage();

                    Mockito.verify(mailSender, Mockito.times(1))
                            .send(Mockito.any(MimeMessage.class));

                    Optional<String> logMessage = logTracker.findMessage(
                            Level.TRACE,
                            "Mail sent to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template"
                    );
                    Assertions.assertTrue(logMessage.isPresent());
                });
    }

    @Test
    @SuppressWarnings({"java:S2925"})
    void givenValidEmailAddresses_whenMailNotSentIn5Seconds_thenLogWarnAboutMailNotSentIn5Seconds() {
        // Given
        AysMail mockMail = new AysMailBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(mailSender.createMimeMessage())
                .thenReturn(Mockito.mock(MimeMessage.class));

        Mockito.doAnswer(invocationOnMock -> {
                    Thread.sleep(10000);
                    throw Mockito.mock(MailException.class);
                })
                .when(mailSender)
                .send(Mockito.any(MimeMessage.class));

        // Then
        mailService.send(mockMail);

        Awaitility.await()
                .atMost(6, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    Mockito.verify(mailSender, Mockito.times(1))
                            .createMimeMessage();

                    Mockito.verify(mailSender, Mockito.times(1))
                            .send(Mockito.any(MimeMessage.class));

                    Optional<String> logMessage = logTracker.findMessage(
                            Level.WARN,
                            "Mail not sent to " + mockMail.getTo() + " in 5 seconds with " + mockMail.getTemplate() + " template"
                    );
                    Assertions.assertTrue(logMessage.isPresent());
                });
    }

    @Test
    void givenValidEmailAddresses_whenMailSendingIgnored_thenLogWarnAboutMailSendingIgnored() {

        // Given
        AysMail mockMail = new AysMailBuilder()
                .withValidValues()
                .withTo(List.of("test@afetyonetimsistemi.test"))
                .build();

        // Then
        mailService.send(mockMail);

        Awaitility.await()
                .atMost(6, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    Mockito.verify(mailSender, Mockito.never())
                            .createMimeMessage();

                    Mockito.verify(mailSender, Mockito.never())
                            .send(Mockito.any(MimeMessage.class));

                    Optional<String> logMessage = logTracker.findMessage(
                            Level.WARN,
                            "Mail sending is ignored for " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template"
                    );
                    Assertions.assertTrue(logMessage.isPresent());
                });
    }

    @Test
    void givenValidEmailAddresses_whenReceivedErrorWhileMailSending_thenLogErrorAboutReceivedErrorWhileMailSending() {
        // Given
        AysMail mockMail = new AysMailBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(mailSender.createMimeMessage())
                .thenReturn(Mockito.mock(MimeMessage.class));

        Mockito.doThrow(Mockito.mock(MailException.class))
                .when(mailSender)
                .send(Mockito.any(MimeMessage.class));

        // Then
        mailService.send(mockMail);

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    Mockito.verify(mailSender, Mockito.times(1))
                            .createMimeMessage();

                    Mockito.verify(mailSender, Mockito.times(1))
                            .send(Mockito.any(MimeMessage.class));

                    Optional<String> logMessage = logTracker.findMessage(
                            Level.ERROR,
                            "Received error while sending mail to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template"
                    );
                    Assertions.assertTrue(logMessage.isPresent());
                });
    }

}
