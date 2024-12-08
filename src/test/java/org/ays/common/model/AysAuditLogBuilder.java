package org.ays.common.model;

import java.time.LocalDateTime;

public class AysAuditLogBuilder extends TestDataBuilder<AysAuditLog> {

    public AysAuditLogBuilder() {
        super(AysAuditLog.class);
    }

    public AysAuditLogBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysAuditLogBuilder withUserId(String userId) {
        data.setUserId(userId);
        return this;
    }

    public AysAuditLogBuilder withoutUserId() {
        data.setUserId(null);
        return this;
    }

    public AysAuditLogBuilder withRequestIpAddress(String requestIpAddress) {
        data.getRequest().setIpAddress(requestIpAddress);
        return this;
    }

    public AysAuditLogBuilder withRequestReferer(String requestReferer) {
        data.getRequest().setReferer(requestReferer);
        return this;
    }

    public AysAuditLogBuilder withRequestHttpMethod(String requestHttpMethod) {
        data.getRequest().setHttpMethod(requestHttpMethod);
        return this;
    }

    public AysAuditLogBuilder withRequestPath(String requestPath) {
        data.getRequest().setPath(requestPath);
        return this;
    }

    public AysAuditLogBuilder withRequestHttpHeader(String requestHttpHeader) {
        data.getRequest().setHttpHeader(requestHttpHeader);
        return this;
    }

    public AysAuditLogBuilder withRequestBody(String requestBody) {
        data.getRequest().setBody(requestBody);
        return this;
    }

    public AysAuditLogBuilder withoutRequestBody() {
        data.getRequest().setBody(null);
        return this;
    }

    public AysAuditLogBuilder withResponseHttpStatusCode(Integer responseHttpStatusCode) {
        data.getResponse().setHttpStatusCode(responseHttpStatusCode);
        return this;
    }

    public AysAuditLogBuilder withResponseBody(String responseBody) {
        data.getResponse().setBody(responseBody);
        return this;
    }

    public AysAuditLogBuilder withRequestedAt(LocalDateTime requestedAt) {
        data.setRequestedAt(requestedAt);
        return this;
    }

    public AysAuditLogBuilder withRespondedAt(LocalDateTime respondedAt) {
        data.setRespondedAt(respondedAt);
        return this;
    }

}
