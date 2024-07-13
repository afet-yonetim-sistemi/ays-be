package org.ays.common.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.AysMail;
import org.ays.common.model.enums.AysMailTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class AysMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysMailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;


    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void start() {
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysMailServiceImpl.class))
                .addAppender(logWatcher);
    }

    @AfterEach
    void detach() {
        ((Logger) LoggerFactory.getLogger(AysMailServiceImpl.class))
                .detachAndStopAllAppenders();
    }


    @Test
    void givenValidMail_whenMailSendingSuccessfully_thenLogTraceAboutMailSentSuccessfully() throws Exception {
        // Given
        AysMail mockMail = AysMail.builder()
                .to(List.of("test@afetyonetimsistemi.org"))
                .template(AysMailTemplate.EXAMPLE)
                .parameters(Map.of("firstName", "World"))
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

                    Assertions.assertEquals(Level.TRACE, this.getLastLogLevel());
                    Assertions.assertEquals(this.getLastLogMessage(), "Mail sent to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template");
                });
    }

    @Test
    @SuppressWarnings({"java:S2925"})
    void givenValidMail_whenMailNotSentIn5Seconds_thenLogWarnAboutMailNotSentIn5Seconds() throws Exception {
        // Given
        AysMail mockMail = AysMail.builder()
                .to(List.of("test@afetyonetimsistemi.org"))
                .template(AysMailTemplate.EXAMPLE)
                .parameters(Map.of("firstName", "World"))
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

                    Assertions.assertEquals(Level.WARN, this.getLastLogLevel());
                    Assertions.assertEquals(this.getLastLogMessage(), "Mail not sent to " + mockMail.getTo() + " in 5 seconds with " + mockMail.getTemplate() + " template");
                });
    }

    @Test
    void givenValidMail_whenReceivedErrorWhileMailSending_thenLogErrorAboutReceivedErrorWhileMailSending() throws Exception {
        // Given
        AysMail mockMail = AysMail.builder()
                .to(List.of("test@afetyonetimsistemi.org"))
                .template(AysMailTemplate.EXAMPLE)
                .parameters(Map.of("firstName", "World"))
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

                    Assertions.assertEquals(Level.ERROR, this.getLastLogLevel());
                    Assertions.assertEquals(this.getLastLogMessage(), "Received error while sending mail to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template");
                });
    }

    private String getLastLogMessage() {
        if (logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = logWatcher.list.size();
        return logWatcher.list.get(logSize - 1).getFormattedMessage();
    }

    private Level getLastLogLevel() {

        if (logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = logWatcher.list.size();
        return logWatcher.list.get(logSize - 1).getLevel();
    }

}
