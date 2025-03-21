package org.ays.common.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.AysMail;
import org.ays.common.model.AysMailBuilder;
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
import java.util.concurrent.TimeUnit;

class AysMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysMailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;


    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void start() {
        this.logWatcher = new ListAppender<>();
        this.logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysMailServiceImpl.class))
                .addAppender(this.logWatcher);
    }

    @AfterEach
    void detach() {
        ((Logger) LoggerFactory.getLogger(AysMailServiceImpl.class))
                .detachAndStopAllAppenders();
    }


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

                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertEquals(lastLogMessage, "Mail sent to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template");
                    Assertions.assertEquals(Level.TRACE, this.getLastLogLevel());
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

                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertEquals(lastLogMessage, "Mail not sent to " + mockMail.getTo() + " in 5 seconds with " + mockMail.getTemplate() + " template");
                    Assertions.assertEquals(Level.WARN, this.getLastLogLevel());
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

                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertEquals(lastLogMessage, "Mail sending is ignored for " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template");
                    Assertions.assertEquals(Level.WARN, this.getLastLogLevel());
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

                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertEquals(lastLogMessage, "Received error while sending mail to " + mockMail.getTo() + " with " + mockMail.getTemplate() + " template");
                    Assertions.assertEquals(Level.ERROR, this.getLastLogLevel());
                });
    }


    private String getLastLogMessage() {

        if (this.logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = this.logWatcher.list.size();
        return this.logWatcher.list.get(logSize - 1).getFormattedMessage();
    }

    private Level getLastLogLevel() {

        if (this.logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = this.logWatcher.list.size();
        return this.logWatcher.list.get(logSize - 1).getLevel();
    }

}
