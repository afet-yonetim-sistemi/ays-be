package org.ays.common.repository.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

class AysAuditLogRepositoryImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogRepositoryImpl auditLogRepository;


    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void start() {
        this.logWatcher = new ListAppender<>();
        this.logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .addAppender(this.logWatcher);
    }

    @AfterEach
    void detach() {
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .detachAndStopAllAppenders();
    }


    @Test
    void givenValidAuditLogEntityForPublicEndpoint_whenAuditLogSaved_thenLogToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("e8981063-b250-4c54-9094-02222865cccf")
                .withoutUserId()
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("GET")
                .withRequestPath("/public/actuator/health")
                .withRequestHttpHeader("Content-Type: application/json")
                .withoutRequestBody()
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"status\\\":\\\"UP\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(2456))
                .build();

        // When
        auditLogRepository.save(mockAuditLogEntity);

        // Then
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertTrue(lastLogMessage.startsWith("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\"" + mockAuditLogEntity.getId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"userId\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestIpAddress\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestReferer\":\"" + mockAuditLogEntity.getRequestReferer() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestHttpMethod\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestPath\":\"" + mockAuditLogEntity.getRequestPath() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestBody\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responseHttpStatusCode\":" + mockAuditLogEntity.getResponseHttpStatusCode()));
                    Assertions.assertTrue(lastLogMessage.contains("\"responseBody\":\"" + mockAuditLogEntity.getResponseBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestedAt\":\"" + mockAuditLogEntity.getRequestedAt() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"respondedAt\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                });
    }

    @Test
    void givenValidAuditLogEntityForPrivateEndpoint_whenAuditLogSaved_thenLogToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("b5596a4d-d1b1-4a7b-aa57-3467255393ed")
                .withUserId("4e064f32-907f-472a-a8e8-a1d8073db0fb")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/9a2f0b36-3ac3-415e-964f-9297ecbe009e")
                .withRequestHttpHeader("Content-Type: application/json")
                .withRequestBody("{\\\"name\\\":\\\"John Doe\\\"}")
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"timestamp\\\":\\\"2021-08-01T12:34:56.789Z\\\",\\\"message\\\":\\\"User updated successfully\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(4485))
                .build();

        // When
        auditLogRepository.save(mockAuditLogEntity);

        // Then
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertNotNull(lastLogMessage);
                    Assertions.assertTrue(lastLogMessage.startsWith("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\"" + mockAuditLogEntity.getId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"userId\":\"" + mockAuditLogEntity.getUserId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestIpAddress\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestReferer\":\"" + mockAuditLogEntity.getRequestReferer() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestHttpMethod\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestPath\":\"" + mockAuditLogEntity.getRequestPath() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestBody\":\"" + mockAuditLogEntity.getRequestBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responseHttpStatusCode\":" + mockAuditLogEntity.getResponseHttpStatusCode()));
                    Assertions.assertTrue(lastLogMessage.contains("\"responseBody\":\"" + mockAuditLogEntity.getResponseBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requestedAt\":\"" + mockAuditLogEntity.getRequestedAt() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"respondedAt\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
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
