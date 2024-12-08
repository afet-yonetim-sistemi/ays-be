package org.ays.common.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents an audit log entity used to store details about HTTP requests and responses.
 */
@Getter
@Setter
public class AysAuditLogEntity {

    private String id;
    private String userId;
    private String requestIpAddress;
    private String requestReferer;
    private String requestHttpMethod;
    private String requestPath;
    private String requestHttpHeader;
    private String requestBody;
    private Integer responseHttpStatusCode;
    private String responseBody;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;


    /**
     * Converts the audit log entity to a JSON string specifically formatted for AWS Kinesis.
     * <p>
     * The method generates a compact JSON string representation of the audit log's fields,
     * ensuring all fields are included with default values (e.g., empty strings for null values).
     * Optional fields are safely handled to avoid {@code null} values in the output.
     * </p>
     *
     * @return a JSON string formatted for Kinesis, representing the current state of the audit log entity
     */
    public String toKinesisJsonString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"user_id\":\"" + Optional.ofNullable(userId).orElse("") + "\"," +
                "\"request_ip_address\":\"" + Optional.ofNullable(requestIpAddress).orElse("") + "\"," +
                "\"request_referer\":\"" + Optional.ofNullable(requestReferer).orElse("") + "\"," +
                "\"request_http_method\":\"" + Optional.ofNullable(requestHttpMethod).orElse("") + "\"," +
                "\"request_path\":\"" + Optional.ofNullable(requestPath).orElse("") + "\"," +
                "\"request_http_header\":\"" + Optional.ofNullable(requestHttpHeader).orElse("") + "\"," +
                "\"request_body\":\"" + Optional.ofNullable(requestBody).orElse("") + "\"," +
                "\"response_http_status_code\":" + Optional.ofNullable(responseHttpStatusCode).orElse(0) + "," +
                "\"response_body\":\"" + Optional.ofNullable(responseBody).orElse("") + "\"," +
                "\"requested_at\":\"" + requestedAt + "\"," +
                "\"responded_at\":\"" + respondedAt + "\"" +
                "}";
    }

}
