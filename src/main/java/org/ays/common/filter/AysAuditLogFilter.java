package org.ays.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysAuditLog;
import org.ays.common.model.request.AysHttpHeader;
import org.ays.common.model.request.AysHttpServletRequest;
import org.ays.common.model.response.AysHttpServletResponse;
import org.ays.common.port.AysAuditLogSavePort;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filter for auditing HTTP requests and responses in the application.
 * <p>
 * This filter captures and logs details of every HTTP request and response,
 * including headers, request and response content, and timestamps. It provides
 * essential information for monitoring and troubleshooting by persisting audit
 * logs through the {@link AysAuditLogSavePort}.
 * <p>
 * The filter executes with an order of {@code 1}, ensuring it is processed early
 * in the filter chain to capture all request and response data.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Records HTTP request and response headers.</li>
 *     <li>Captures request and response timestamps for performance analysis.</li>
 *     <li>Persists audit logs for future reference.</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * Automatically applied to all incoming HTTP requests and responses
 * via Spring's filter mechanism.
 */
@Order(1)
@Component
@RequiredArgsConstructor
public class AysAuditLogFilter extends OncePerRequestFilter {

    private final AysAuditLogSavePort auditLogSavePort;
    private final AysIdentity identity;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final AysHttpServletRequest aysHttpServletRequest = new AysHttpServletRequest(httpServletRequest);
        final AysHttpHeader aysHttpHeader = aysHttpServletRequest.getHeader();
        final AysHttpServletResponse aysHttpServletResponse = new AysHttpServletResponse(httpServletResponse);

        final LocalDateTime requestedAt = LocalDateTime.now();
        try {
            filterChain.doFilter(aysHttpServletRequest, aysHttpServletResponse);
        } finally {

            final AysAuditLog.AysAuditLogBuilder auditLogBuilder = AysAuditLog.builder()
                    .aysHttpHeader(aysHttpHeader)
                    .aysHttpServletRequest(aysHttpServletRequest)
                    .aysHttpServletResponse(aysHttpServletResponse)
                    .requestedAt(requestedAt)
                    .respondedAt(LocalDateTime.now());

            if (identity.isAuthenticated()) {
                auditLogBuilder.userId(identity.getUserId());
            }

            auditLogSavePort.save(auditLogBuilder.build());
        }
    }

}
