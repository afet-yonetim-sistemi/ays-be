package org.ays.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ays.common.model.response.AysErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;

/**
 * Custom authentication entry point that implements the {@link AuthenticationEntryPoint} interface.
 * It sends an "Unauthorized" response with the HTTP status code 401 (SC_UNAUTHORIZED)
 * for unauthorized requests.
 */
@Component
public class AysAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AysAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }
      /**
     * Handles the unauthorized request by sending an "Unauthorized"
     * response with the HTTP status code 401 (SC_UNAUTHORIZED).
     *
     * @param httpServletRequest      the {@link HttpServletRequest} object representing the incoming request
     * @param httpServletResponse     the {@link HttpServletResponse} object used to send the response
     * @param authenticationException the {@link AuthenticationException} that occurred during authentication
     * @throws IOException if an I/O error occurs while sending the response
     */

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException authenticationException) throws IOException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        final AysErrorResponse error = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .message(authenticationException.getMessage())
                .isSuccess(false)
                .build();

        final String responseBody = objectMapper.writeValueAsString(error);
        httpServletResponse.getOutputStream().write(responseBody.getBytes());
    }
}

