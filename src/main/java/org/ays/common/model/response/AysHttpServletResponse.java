package org.ays.common.model.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A custom HTTP servlet response wrapper that extends {@link ContentCachingResponseWrapper}.
 * <p>
 * This class is designed to cache the body content of the HTTP response for later retrieval.
 * It provides a method to obtain the response body as a String while ensuring that the body
 * is properly copied to the original response.
 * </p>
 * <p>
 * The character encoding of the response is set to UTF-8 if the encoding is not explicitly defined.
 * </p>
 */
public class AysHttpServletResponse extends ContentCachingResponseWrapper {

    /**
     * Cached body content as a String to avoid re-computation.
     */
    private String cachedBodyContent;

    /**
     * Constructs a new {@link AysHttpServletResponse} instance.
     *
     * @param httpServletResponse the original {@link HttpServletResponse} to be wrapped.
     */
    public AysHttpServletResponse(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
    }

    /**
     * Returns the character encoding for the response.
     * <p>
     * If the encoding is not set in the original response, it defaults to {@code UTF-8}.
     * </p>
     *
     * @return the character encoding of the response.
     */
    @Override
    public String getCharacterEncoding() {
        final String encoding = super.getCharacterEncoding();

        if (encoding != null) {
            return encoding;
        }

        return StandardCharsets.UTF_8.name();
    }

    /**
     * Retrieves the body content of the response as a String.
     * <p>
     * This method checks if the body content has already been cached. If it has, it returns
     * the cached content. If not, it reads the body content from the response, encodes it as
     * a String using UTF-8, and caches it for future requests. After reading the body content,
     * it ensures that the original response body is copied to the response stream.
     * </p>
     *
     * @return the body content of the response as a String, or an empty string if an
     * {@link IOException} occurs while copying the body to the response.
     */
    @SuppressWarnings("java:S1941")
    public String getBody() {

        if (this.cachedBodyContent != null) {
            return this.cachedBodyContent;
        }

        final byte[] bodyByteArray = this.getContentAsByteArray();

        final String bodyContent = new String(bodyByteArray, StandardCharsets.UTF_8);

        try {
            this.copyBodyToResponse();
        } catch (IOException e) {
            return "";
        }

        this.cachedBodyContent = bodyContent;
        return bodyContent;
    }

}
