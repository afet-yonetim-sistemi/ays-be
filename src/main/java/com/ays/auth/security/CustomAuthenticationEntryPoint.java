package com.ays.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}
