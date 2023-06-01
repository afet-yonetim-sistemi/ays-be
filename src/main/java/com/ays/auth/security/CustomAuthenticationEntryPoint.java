package com.ays.auth.security;

import com.ays.common.util.exception.model.AysError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles the unauthorized request by sending an "Unauthorized"
     * response with the HTTP status code 401 (SC_UNAUTHORIZED).
     *
     * @param request       the {@link HttpServletRequest} object representing the incoming request
     * @param response      the {@link HttpServletResponse} object used to send the response
     * @param authException the {@link AuthenticationException} that occurred during authentication
     * @throws IOException if an I/O error occurs while sending the response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        AysError aysError = AysError.builder()
                .header(AysError.Header.AUTH_ERROR.getName())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();
        ObjectMapper obj = new ObjectMapper();
        obj.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        String res = obj.writer(DateFormat.getDateInstance()).writeValueAsString(aysError);
        response.setStatus(401);
        response.getOutputStream().write(res.getBytes());
    }

}
