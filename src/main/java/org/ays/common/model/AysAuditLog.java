package org.ays.common.model;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysHttpHeader;
import org.ays.common.model.request.AysHttpServletRequest;
import org.ays.common.model.response.AysHttpServletResponse;
import org.ays.common.util.AysJsonUtil;
import org.ays.common.util.AysRandomUtil;

import java.time.LocalDateTime;

/**
 * Represents an audit log for tracking HTTP request and response details.
 * This class captures metadata about the user request, the request itself,
 * the server's response, and timestamps for auditing purposes.
 */
@Getter
public class AysAuditLog {

    private String id;
    private String userId;
    private Request request;
    private Response response;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;


    /**
     * Represents the details of an HTTP request in the audit log.
     */
    @Getter
    @Setter
    public static class Request {
        private String ipAddress;
        private String referer;
        private String httpMethod;
        private String path;
        private String httpHeader;
        private String body;
    }

    /**
     * Represents the details of an HTTP response in the audit log.
     */
    @Getter
    @Setter
    public static class Response {
        private Integer httpStatusCode;
        private String body;
    }


    public static AysAuditLogBuilder builder() {
        return new AysAuditLogBuilder();
    }

    /**
     * Provides a builder for creating instances of {@link AysAuditLog}.
     */
    public static class AysAuditLogBuilder {

        private final String id;
        private String userId;
        private final Request request;
        private final Response response;
        private LocalDateTime respondedAt;
        private LocalDateTime requestedAt;

        public AysAuditLogBuilder() {
            this.id = AysRandomUtil.generateUUID();
            this.request = new Request();
            this.response = new Response();
        }

        public AysAuditLogBuilder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public AysAuditLogBuilder aysHttpServletRequest(final AysHttpServletRequest aysHttpServletRequest) {

            this.request.httpMethod = aysHttpServletRequest.getMethod();
            this.request.path = aysHttpServletRequest.getPath();
            this.request.body = AysJsonUtil.toUnformattedJson(aysHttpServletRequest.getBody())
                    .replaceAll("\"", "\\\\\"");
            return this;
        }

        public AysAuditLogBuilder aysHttpHeader(final AysHttpHeader aysHttpHeader) {

            this.request.ipAddress = aysHttpHeader.getIpAddress();
            this.request.referer = aysHttpHeader.getReferer();
            this.request.httpHeader = aysHttpHeader.getAll();
            return this;
        }

        public AysAuditLogBuilder aysHttpServletResponse(final AysHttpServletResponse aysHttpServletResponse) {

            this.response.httpStatusCode = aysHttpServletResponse.getStatus();
            this.response.body = AysJsonUtil.toUnformattedJson(aysHttpServletResponse.getBody())
                    .replaceAll("\"", "\\\\\"");
            return this;
        }

        public AysAuditLogBuilder requestedAt(final LocalDateTime requestedAt) {
            this.requestedAt = requestedAt;
            return this;
        }

        public AysAuditLogBuilder respondedAt(final LocalDateTime respondedAt) {
            this.respondedAt = respondedAt;
            return this;
        }

        public AysAuditLog build() {
            final AysAuditLog auditLog = new AysAuditLog();
            auditLog.id = this.id;
            auditLog.userId = this.userId;
            auditLog.request = this.request;
            auditLog.response = this.response;
            auditLog.requestedAt = this.requestedAt;
            auditLog.respondedAt = this.respondedAt;
            return auditLog;
        }

    }

}
