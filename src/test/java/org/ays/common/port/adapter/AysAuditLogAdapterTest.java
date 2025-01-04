package org.ays.common.port.adapter;

import org.ays.AysUnitTest;
import org.ays.common.model.AysAuditLog;
import org.ays.common.model.AysAuditLogBuilder;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.repository.AysAuditLogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class AysAuditLogAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogAdapter auditLogAdapter;

    @Mock
    private AysAuditLogRepository auditLogRepository;


    @Test
    void givenValidAuditLogEntityForPublicEndpoint_whenAuditLogSaved_thenDoNothing() {

        // Given
        AysAuditLog mockAuditLog = new AysAuditLogBuilder()
                .withId("3b06bc18-aea8-4354-9b0b-43036bf67bd2")
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
        Mockito.doNothing()
                .when(auditLogRepository)
                .save(Mockito.any(AysAuditLogEntity.class));

        // Then
        auditLogAdapter.save(mockAuditLog);

        // Verify
        Mockito.verify(auditLogRepository, Mockito.times(1))
                .save(Mockito.any(AysAuditLogEntity.class));
    }

    @Test
    void givenValidAuditLogEntityForPrivateEndpoint_whenAuditLogSaved_thenDoNothing() {

        // Given
        AysAuditLog mockAuditLog = new AysAuditLogBuilder()
                .withId("614e47f3-1a02-4ef8-a9f3-8971cf2b244f")
                .withUserId("a15bc369-5f4e-4480-95a1-8e853aa72b7c")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/445a3a7a-42bb-400f-97fe-555b4892b4a8")
                .withRequestHttpHeader("Content-Type: application/json")
                .withRequestBody("{\\\"name\\\":\\\"John Doe\\\"}")
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"timestamp\\\":\\\"2021-08-01T12:34:56.789Z\\\",\\\"message\\\":\\\"User updated successfully\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusSeconds(4485))
                .build();

        // When
        Mockito.doNothing()
                .when(auditLogRepository)
                .save(Mockito.any(AysAuditLogEntity.class));

        // Then
        auditLogAdapter.save(mockAuditLog);

        // Verify
        Mockito.verify(auditLogRepository, Mockito.times(1))
                .save(Mockito.any(AysAuditLogEntity.class));
    }

}
