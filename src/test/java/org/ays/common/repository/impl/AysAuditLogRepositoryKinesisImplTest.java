package org.ays.common.repository.impl;

import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.time.LocalDateTime;

class AysAuditLogRepositoryKinesisImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogRepositoryKinesisImpl auditLogRepository;

    @Mock
    private KinesisClient kinesisClient;


    @Test
    void givenValidAuditLogEntityForPublicEndpoint_whenAuditLogSavedToKinesis_thenDoNothing() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("a8502816-4a1b-403c-9d83-b93b7ce2781a")
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
        PutRecordResponse mockPutRecordResponse = Mockito.mock(PutRecordResponse.class);
        Mockito.when(kinesisClient.putRecord(Mockito.any(PutRecordRequest.class)))
                .thenReturn(mockPutRecordResponse);

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        // Verify
        Mockito.verify(kinesisClient, Mockito.times(1))
                .putRecord(Mockito.any(PutRecordRequest.class));
    }

    @Test
    void givenValidAuditLogEntityForPrivateEndpoint_whenAuditLogSavedToKinesis_thenDoNothing() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("1005822e-eaf7-4cfa-b0cf-41de5e5da4dd")
                .withUserId("d70d3645-bd79-42a9-9735-1f2081b56791")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/1b00645c-73db-496a-acbc-504ff970dcfc")
                .withRequestHttpHeader("Content-Type: application/json")
                .withRequestBody("{\\\"name\\\":\\\"John Doe\\\"}")
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"timestamp\\\":\\\"2021-08-01T12:34:56.789Z\\\",\\\"message\\\":\\\"User updated successfully\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(4485))
                .build();

        // When
        PutRecordResponse mockPutRecordResponse = Mockito.mock(PutRecordResponse.class);
        Mockito.when(kinesisClient.putRecord(Mockito.any(PutRecordRequest.class)))
                .thenReturn(mockPutRecordResponse);

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        // Verify
        Mockito.verify(kinesisClient, Mockito.times(1))
                .putRecord(Mockito.any(PutRecordRequest.class));
    }

}
