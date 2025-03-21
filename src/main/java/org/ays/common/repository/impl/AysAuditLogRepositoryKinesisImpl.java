package org.ays.common.repository.impl;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.repository.AysAuditLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

/**
 * Implementation of the {@link AysAuditLogRepository} interface for saving audit log entities to an AWS Kinesis stream.
 * This implementation is active only when the application is running with the "kinesis" profile.
 */
@Slf4j
@Repository
@Profile("aws")
@RequiredArgsConstructor
class AysAuditLogRepositoryKinesisImpl implements AysAuditLogRepository {

    private final KinesisClient kinesisClient;


    @Value("${aws.kinesis.audit-log.stream-name}")
    private String streamName;


    /**
     * Saves the given {@link AysAuditLogEntity} to the configured AWS Kinesis stream.
     *
     * @param auditLogEntity the audit log entity to be saved
     */
    @Override
    public void save(final AysAuditLogEntity auditLogEntity) {

        final String auditLogJsonString = auditLogEntity.toJsonString();
        if (this.isParseableToJson(auditLogJsonString)) {
            log.warn("Audit log JSON is not parseable: {}", auditLogJsonString);
            return;
        }

        final SdkBytes sdkBytes = SdkBytes.fromUtf8String(auditLogEntity.toKinesisJsonString());

        final PutRecordRequest putRecordRequest = PutRecordRequest.builder()
                .streamName(this.streamName)
                .partitionKey(auditLogEntity.getId())
                .data(sdkBytes)
                .build();

        kinesisClient.putRecord(putRecordRequest);
    }

    private boolean isParseableToJson(String jsonString) {
        try {
            JsonParser.parseString(jsonString);
            return false;
        } catch (JsonSyntaxException e) {
            return true;
        }
    }

}
