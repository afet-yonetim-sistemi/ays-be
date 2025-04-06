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

import java.util.List;

/**
 * Implementation of the {@link AysAuditLogRepository} interface for saving audit log entities to an AWS Kinesis stream.
 * This implementation is active only when the application is running with the "kinesis" profile.
 */
@Slf4j
@Repository
@Profile("aws")
@RequiredArgsConstructor
class AysAuditLogRepositoryKinesisImpl extends AysAbstractAuditLogRepository {

    private final KinesisClient kinesisClient;


    @Value("${aws.kinesis.audit-log.stream-name}")
    private String streamName;


    private static final List<String> EXCLUDED_PATH_PREFIXES = List.of(
            "/public/actuator"
    );


    /**
     * Persists an {@link AysAuditLogEntity} to an AWS Kinesis stream.
     * <p>
     * This method first converts the audit log entity to a JSON string using {@link AysAuditLogEntity#toKinesisJsonString()} ()}
     * and checks whether the resulting string is valid JSON. If the JSON string is not parseable,
     * a warning is logged and the method exits without sending data to Kinesis.
     * Otherwise, the audit log entity is converted to a Kinesis-specific JSON string via
     * {@link AysAuditLogEntity#toKinesisJsonString()}, wrapped in {@link SdkBytes}, and sent
     * to the Kinesis stream using a {@link PutRecordRequest}.
     * </p>
     *
     * @param auditLogEntity the audit log entity to be saved
     */
    @Override
    public void save(final AysAuditLogEntity auditLogEntity) {

        super.save(auditLogEntity);

        final String kinesisJsonString = auditLogEntity.toKinesisJsonString();
        if (this.isParseableToJson(kinesisJsonString)) {
            log.warn("Audit log does not parse to Kinesis JSON String: {}", kinesisJsonString);
            return;
        }

        final String requestPath = auditLogEntity.getRequestPath();
        final boolean isRequestPathExcluded = EXCLUDED_PATH_PREFIXES.stream().anyMatch(requestPath::startsWith);
        if (isRequestPathExcluded) {
            log.trace("Audit log will not be sent to Kinesis because the request path is excluded: {}", requestPath);
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

    /**
     * Determines whether the provided string is not parseable as valid JSON.
     * <p>
     * This method attempts to parse the given {@code jsonString} using {@link JsonParser#parseString(String)}.
     * If the parsing succeeds, the method returns {@code false}, indicating that the string is valid JSON.
     * If a {@link JsonSyntaxException} is thrown during parsing, the method returns {@code true},
     * indicating that the string is not parseable as valid JSON.
     * </p>
     *
     * @param jsonString the string to test for JSON parseability
     * @return {@code true} if the string is not valid JSON; {@code false} if it is valid JSON
     */
    private boolean isParseableToJson(String jsonString) {
        try {
            JsonParser.parseString(jsonString);
            return false;
        } catch (JsonSyntaxException e) {
            return true;
        }
    }

}
