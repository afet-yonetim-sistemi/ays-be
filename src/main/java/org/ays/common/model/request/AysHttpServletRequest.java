package org.ays.common.model.request;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * AysHttpServletRequest is a custom implementation of HttpServletRequest
 * that wraps an existing HttpServletRequest to capture and cache
 * the request's path, method, body, and headers.
 * This class extends ContentCachingRequestWrapper and is designed
 * to provide access to the HTTP request information in a convenient
 * manner.
 *
 * <p>This class captures the body of the request at the time of
 * construction, allowing for repeated access to the request data
 * without loss of information. It also adapts the input stream
 * and reader to use the cached body content.</p>
 *
 * <p>Usage of this class is particularly useful in scenarios
 * where request data needs to be inspected, logged, or processed
 * without affecting the original request.</p>
 */
@Getter
public class AysHttpServletRequest extends ContentCachingRequestWrapper implements HttpServletRequest {

    private final String path;
    private final String method;
    private final String body;
    private final AysHttpHeader header;

    /**
     * Constructs a new AysHttpServletRequest wrapping the given
     * HttpServletRequest. This constructor reads and stores the request
     * path, method, body, and headers.
     *
     * @param httpServletRequest the original HttpServletRequest to wrap
     * @throws IOException if an I/O error occurs while reading the request body
     */
    public AysHttpServletRequest(final HttpServletRequest httpServletRequest) throws IOException {
        super(httpServletRequest);
        this.path = httpServletRequest.getRequestURI();
        this.method = httpServletRequest.getMethod();
        this.body = httpServletRequest.getReader().lines().collect(Collectors.joining());
        this.header = new AysHttpHeader(httpServletRequest);
    }

    /**
     * Returns a ServletInputStream that reads from the cached body.
     * This allows repeated access to the request body as if it
     * were a regular input stream.
     *
     * @return a ServletInputStream to read the body content
     */
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Do nothing
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * Returns a BufferedReader that reads from the cached body.
     * This allows for convenient reading of the body content
     * as character data.
     *
     * @return a BufferedReader to read the body content
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

}
