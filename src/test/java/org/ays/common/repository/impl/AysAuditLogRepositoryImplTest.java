package org.ays.common.repository.impl;

import ch.qos.logback.classic.Level;
import org.awaitility.Awaitility;
import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class AysAuditLogRepositoryImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogRepositoryImpl auditLogRepository;

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
                    Optional<String> logMessage = logTracker.findMessage(Level.DEBUG, "Audit log saved: ");
                    Assertions.assertTrue(logMessage.isPresent());
                    Assertions.assertTrue(logMessage.get().startsWith("Audit log saved: "));
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
                    Optional<String> logMessage = logTracker.findMessage(Level.DEBUG, "Audit log saved: ");
                    Assertions.assertTrue(logMessage.isPresent());
                    Assertions.assertTrue(logMessage.get().startsWith("Audit log saved: "));
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
