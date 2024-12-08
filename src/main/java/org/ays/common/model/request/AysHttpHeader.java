package org.ays.common.model.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AysHttpHeader is a specialized HTTP headers handler that captures and manages
 * HTTP headers related to authorization, client IP address, referrer, and all other headers
 * present in an {@link HttpServletRequest}. It also provides utility methods for handling bearer tokens.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("java:S1845")
public final class AysHttpHeader extends HttpHeaders {

    @Serial
    private static final long serialVersionUID = 5318618587701377033L;

    private final String authorization;
    private final String ipAddress;
    private final String referer;
    private final String all;

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * Constructs an instance of AysHttpHeader by extracting values from the provided
     * {@link HttpServletRequest}.
     *
     * @param httpServletRequest the HTTP servlet request containing the headers
     */
    public AysHttpHeader(final HttpServletRequest httpServletRequest) {
        this.authorization = httpServletRequest.getHeader(AUTHORIZATION);
        this.referer = httpServletRequest.getHeader(REFERER);
        this.ipAddress = this.getClientIpAddress(httpServletRequest);
        this.all = this.getAllHeaders(httpServletRequest);
    }

    /**
     * Retrieves all header information from the given request, excluding the
     * referrer and X-Forwarded-For headers.
     *
     * @param httpServletRequest the HTTP servlet request
     * @return a string representation of all headers (excluding REFERRER and X-Forwarded-For)
     */
    private String getAllHeaders(final HttpServletRequest httpServletRequest) {
        final List<String> headerNames = Collections.list(httpServletRequest.getHeaderNames());
        headerNames.remove(REFERER.toLowerCase());
        headerNames.remove(X_FORWARDED_FOR.toLowerCase());
        return headerNames
                .stream()
                .map(headerName -> headerName + ":" + httpServletRequest.getHeader(headerName))
                .collect(Collectors.joining("; "));
    }

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Checks if the authorization header contains a Bearer token.
     *
     * @return true if the authorization header is present and starts with "Bearer ", otherwise false
     */
    public boolean hasBearerToken() {
        return StringUtils.hasText(this.authorization) && this.authorization.startsWith(TOKEN_PREFIX);
    }

    /**
     * Extracts and returns the Bearer token from the authorization header.
     *
     * @return the Bearer token without the "Bearer " prefix, or an empty string if not present
     */
    public String getBearerToken() {
        return this.authorization.replace(TOKEN_PREFIX, "");
    }

    /**
     * Retrieves the client IP address from the provided request, using the X-Forwarded-For header
     * if available; otherwise falls back to the remote address.
     *
     * @param httpServletRequest the HTTP servlet request
     * @return the client's IP address as a string
     */
    private String getClientIpAddress(final HttpServletRequest httpServletRequest) {

        final Optional<String> ipAddressFromHeader = Optional
                .ofNullable(httpServletRequest.getHeader(AysHttpHeader.X_FORWARDED_FOR));

        return ipAddressFromHeader
                .map(ip -> ip.split(",")[0].trim())
                .orElseGet(() -> httpServletRequest.getRemoteAddr().trim());
    }

}
