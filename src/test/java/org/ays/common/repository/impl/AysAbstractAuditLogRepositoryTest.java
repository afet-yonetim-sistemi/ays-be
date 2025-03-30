package org.ays.common.repository.impl;

import ch.qos.logback.classic.Level;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class AysAbstractAuditLogRepositoryTest extends AysUnitTest {

    private AysAbstractAuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        this.auditLogRepository = new AysAuditLogRepositoryImpl();
    }

    @Test
    void givenValidAuditLogEntityForPublicEndpoint_whenAuditLogSaved_thenLogToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("d9cd968c-f6cc-481d-b91a-cf119b9ed94d")
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
                    String logMessagePrefix = "Audit log saved: ";
                    Optional<String> logMessage = logTracker.findMessage(Level.DEBUG, logMessagePrefix);
                    Assertions.assertTrue(logMessage.isPresent());
                    Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));
                    Assertions.assertTrue(logMessage.get().contains("\"id\":\"" + mockAuditLogEntity.getId() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"userId\":\"\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestIpAddress\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestReferer\":\"" + mockAuditLogEntity.getRequestReferer() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestHttpMethod\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestPath\":\"" + mockAuditLogEntity.getRequestPath() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestBody\":\"\""));
                    Assertions.assertTrue(logMessage.get().contains("\"responseHttpStatusCode\":" + mockAuditLogEntity.getResponseHttpStatusCode() + ","));
                    Assertions.assertTrue(logMessage.get().contains("\"responseBody\":\"" + mockAuditLogEntity.getResponseBody() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestedAt\":\"" + mockAuditLogEntity.getRequestedAt() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"respondedAt\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
                });
    }

    @Test
    void givenValidAuditLogEntityForPrivateEndpoint_whenAuditLogSaved_thenLogToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("bb6552cc-43ad-4bb8-b8ff-72f92ebc207e")
                .withUserId("555f8b44-5004-408b-a8b3-4545e75187a3")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/12dc9611-8ce6-4cf8-b0cc-e5e9511cc7cb")
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
                    String logMessagePrefix = "Audit log saved: ";
                    Optional<String> logMessage = logTracker.findMessage(Level.DEBUG, logMessagePrefix);
                    Assertions.assertTrue(logMessage.isPresent());
                    Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));
                    Assertions.assertTrue(logMessage.get().contains("\"id\":\"" + mockAuditLogEntity.getId() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"userId\":\"" + mockAuditLogEntity.getUserId() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestIpAddress\":\"" + mockAuditLogEntity.getRequestIpAddress() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestReferer\":\"" + mockAuditLogEntity.getRequestReferer() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestHttpMethod\":\"" + mockAuditLogEntity.getRequestHttpMethod() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestPath\":\"" + mockAuditLogEntity.getRequestPath() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestBody\":\"" + mockAuditLogEntity.getRequestBody() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"responseHttpStatusCode\":" + mockAuditLogEntity.getResponseHttpStatusCode() + ","));
                    Assertions.assertTrue(logMessage.get().contains("\"responseBody\":\"" + mockAuditLogEntity.getResponseBody() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"requestedAt\":\"" + mockAuditLogEntity.getRequestedAt() + "\","));
                    Assertions.assertTrue(logMessage.get().contains("\"respondedAt\":\"" + mockAuditLogEntity.getRespondedAt() + "\""));
                });
    }

}
