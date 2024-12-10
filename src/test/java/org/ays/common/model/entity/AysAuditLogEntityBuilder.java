package org.ays.common.model.entity;

import org.ays.common.model.TestDataBuilder;

import java.time.LocalDateTime;

public class AysAuditLogEntityBuilder extends TestDataBuilder<AysAuditLogEntity> {

    public AysAuditLogEntityBuilder() {
        super(AysAuditLogEntity.class);
    }

    public AysAuditLogEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysAuditLogEntityBuilder withUserId(String userId) {
        data.setUserId(userId);
        return this;
    }

    public AysAuditLogEntityBuilder withoutUserId() {
        data.setUserId(null);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestIpAddress(String requestIpAddress) {
        data.setRequestIpAddress(requestIpAddress);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestReferer(String requestReferer) {
        data.setRequestReferer(requestReferer);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestHttpMethod(String requestHttpMethod) {
        data.setRequestHttpMethod(requestHttpMethod);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestPath(String requestPath) {
        data.setRequestPath(requestPath);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestHttpHeader(String requestHttpHeader) {
        data.setRequestHttpHeader(requestHttpHeader);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestBody(String requestBody) {
        data.setRequestBody(requestBody);
        return this;
    }

    public AysAuditLogEntityBuilder withoutRequestBody() {
        data.setRequestBody(null);
        return this;
    }

    public AysAuditLogEntityBuilder withResponseHttpStatusCode(Integer responseHttpStatusCode) {
        data.setResponseHttpStatusCode(responseHttpStatusCode);
        return this;
    }

    public AysAuditLogEntityBuilder withResponseBody(String responseBody) {
        data.setResponseBody(responseBody);
        return this;
    }

    public AysAuditLogEntityBuilder withRequestedAt(LocalDateTime requestedAt) {
        data.setRequestedAt(requestedAt);
        return this;
    }

    public AysAuditLogEntityBuilder withRespondedAt(LocalDateTime respondedAt) {
        data.setRespondedAt(respondedAt);
        return this;
    }

}
