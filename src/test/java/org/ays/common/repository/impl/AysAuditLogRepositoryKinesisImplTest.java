package org.ays.common.repository.impl;

import ch.qos.logback.classic.Level;
import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.time.LocalDateTime;
import java.util.Optional;

class AysAuditLogRepositoryKinesisImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogRepositoryKinesisImpl auditLogRepository;

    @Mock
    private KinesisClient kinesisClient;


    @Test
    void givenValidAuditLogEntity_whenRequestPathIsPrivate_thenSendAuditLogJSONStringToKinesis() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("1005822e-eaf7-4cfa-b0cf-41de5e5da4dd")
                .withUserId("d70d3645-bd79-42a9-9735-1f2081b56791")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/institution/user/1b00645c-73db-496a-acbc-504ff970dcfc")
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

    @ValueSource(strings = {
            "/public/actuator",
            "/public/actuator/info",
            "/public/actuator/health",
            "/public/actuator/prometheus"
    })
    @ParameterizedTest
    void givenValidAuditLogEntity_whenRequestPathIsExcluded_thenLogMessageToConsole(String mockRequestPath) {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("a8502816-4a1b-403c-9d83-b93b7ce2781a")
                .withoutUserId()
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("GET")
                .withRequestPath(mockRequestPath)
                .withRequestHttpHeader("Content-Type: application/json")
                .withoutRequestBody()
                .withResponseHttpStatusCode(200)
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(2456))
                .build();

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        String logMessagePrefix = "Audit log will not be sent to Kinesis because the request path is excluded: ";
        Optional<String> logMessage = logTracker.findMessage(Level.TRACE, logMessagePrefix);
        Assertions.assertTrue(logMessage.isPresent());
        Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));
        Assertions.assertTrue(logMessage.get().endsWith(mockRequestPath));

        // Verify
        Mockito.verify(kinesisClient, Mockito.never())
                .putRecord(Mockito.any(PutRecordRequest.class));
    }

    @ValueSource(ints = {
            429
    })
    @ParameterizedTest
    void givenValidAuditLogEntity_whenResponseHttpStatusCodeIsExcluded_thenLogMessageToConsole(Integer mockResponseHttpStatusCode) {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("94cc0aa6-c430-4286-93d2-afc9ae3be55b")
                .withoutUserId()
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("GET")
                .withRequestPath("/api/v1/institution/user/a3df6f73-b459-413f-8d93-8f99ac1a7c45")
                .withRequestHttpHeader("Content-Type: application/json")
                .withoutRequestBody()
                .withResponseHttpStatusCode(mockResponseHttpStatusCode)
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(2456))
                .build();

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        String logMessagePrefix = "Audit log will not be sent to Kinesis because the response http status code is excluded: ";
        Optional<String> logMessage = logTracker.findMessage(Level.TRACE, logMessagePrefix);
        Assertions.assertTrue(logMessage.isPresent());
        Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));
        Assertions.assertTrue(logMessage.get().endsWith(mockResponseHttpStatusCode.toString()));

        // Verify
        Mockito.verify(kinesisClient, Mockito.never())
                .putRecord(Mockito.any(PutRecordRequest.class));
    }

    @Test
    void givenValidAuditLogEntity_whenAuditLogIsNotParsableToJson_thenLogAuditLogKinesisJSONToConsole() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("1005822e-eaf7-4cfa-b0cf-41de5e5da4dd")
                .withUserId("d70d3645-bd79-42a9-9735-1f2081b56791")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/institution/user/1b00645c-73db-496a-acbc-504ff970dcfc")
                .withRequestHttpHeader("Content-Type: application/json\1\1***\\\\\\")
                .withRequestBody("{\\\"name\\\":\\\"John Doe\\\"}")
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"timestamp\\\":\\\"2021-08-01T12:34:56.789Z\\\",\\\"message\\\":\\\"User updated successfully\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(4485))
                .build();

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        String logMessagePrefix = "Audit log does not parse to Kinesis JSON String: ";
        Optional<String> logMessage = logTracker.findMessage(Level.WARN, logMessagePrefix);
        Assertions.assertTrue(logMessage.isPresent());
        Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));

        // Verify
        Mockito.verify(kinesisClient, Mockito.never())
                .putRecord(Mockito.any(PutRecordRequest.class));
    }

}
