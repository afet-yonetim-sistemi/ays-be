package org.ays.common.repository.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.ays.AysUnitTest;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.entity.AysAuditLogEntityBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.time.LocalDateTime;

class AysAuditLogRepositoryKinesisImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuditLogRepositoryKinesisImpl auditLogRepository;

    @Mock
    private KinesisClient kinesisClient;


    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void start() {
        this.logWatcher = new ListAppender<>();
        this.logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryKinesisImpl.class))
                .addAppender(this.logWatcher);
    }

    @AfterEach
    void detach() {
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryKinesisImpl.class))
                .detachAndStopAllAppenders();
    }


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

    @Test
    void givenValidAuditLogEntity_whenAuditLogIsNotParsableToJson_thenDoNothing() {

        // Given
        AysAuditLogEntity mockAuditLogEntity = new AysAuditLogEntityBuilder()
                .withId("1005822e-eaf7-4cfa-b0cf-41de5e5da4dd")
                .withUserId("d70d3645-bd79-42a9-9735-1f2081b56791")
                .withRequestIpAddress("127.0.0.1")
                .withRequestReferer("http://localhost:3000")
                .withRequestHttpMethod("PUT")
                .withRequestPath("/api/v1/user/1b00645c-73db-496a-acbc-504ff970dcfc")
                .withRequestHttpHeader("Content-Type: application/json\1\1***\\\\\\")
                .withRequestBody("{\\\"name\\\":\\\"John Doe\\\"}")
                .withResponseHttpStatusCode(200)
                .withResponseBody("{\\\"timestamp\\\":\\\"2021-08-01T12:34:56.789Z\\\",\\\"message\\\":\\\"User updated successfully\\\"}")
                .withRequestedAt(LocalDateTime.now())
                .withRespondedAt(LocalDateTime.now().plusNanos(4485))
                .build();

        // Then
        auditLogRepository.save(mockAuditLogEntity);

        String lastLogMessage = this.getLastLogMessage();
        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.startsWith("Audit log JSON is not parseable: "));

        Assertions.assertEquals(Level.WARN, this.getLastLogLevel());

        // Verify
        Mockito.verify(kinesisClient, Mockito.never())
                .putRecord(Mockito.any(PutRecordRequest.class));
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
