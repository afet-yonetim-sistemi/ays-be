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
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .addAppender(logWatcher);
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
                .withId("d91657b3-b6e9-41ac-aa4f-f833d4c08aee")
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertTrue(lastLogMessage.contains("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\"" + mockAuditLogEntity.getId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"user_id\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_ip_address\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_referer\":\"" + mockAuditLogEntity.getRequestReferer() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_http_method\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_path\":\"" + mockAuditLogEntity.getRequestPath() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_body\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_http_status_code\":" + mockAuditLogEntity.getResponseHttpStatusCode()));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_body\":\"" + mockAuditLogEntity.getResponseBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requested_at\":\"" + mockAuditLogEntity.getRequestedAt() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responded_at\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
                });
    }

    @Test
    void givenValidAuditLogEntityForPrivateEndpoint_whenAuditLogSaved_thenLogToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("d91657b3-b6e9-41ac-aa4f-f833d4c08aee")
                .withUserId("5a8a803d-d4e7-4d60-9d66-1c54ce199236")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/77e7abe9-dd4f-4ff8-b49a-3812611ff49b")
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertTrue(lastLogMessage.contains("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\"" + mockAuditLogEntity.getId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"user_id\":\"" + mockAuditLogEntity.getUserId() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_ip_address\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_referer\":\"" + mockAuditLogEntity.getRequestReferer() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_http_method\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_path\":\"" + mockAuditLogEntity.getRequestPath() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_body\":\"" + mockAuditLogEntity.getRequestBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_http_status_code\":" + mockAuditLogEntity.getResponseHttpStatusCode()));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_body\":\"" + mockAuditLogEntity.getResponseBody() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requested_at\":\"" + mockAuditLogEntity.getRequestedAt() + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responded_at\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
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
