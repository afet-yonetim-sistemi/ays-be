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