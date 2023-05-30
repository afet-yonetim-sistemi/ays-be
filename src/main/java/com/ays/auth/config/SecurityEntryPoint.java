package com.ays.auth.config;

import com.ays.auth.model.dto.response.AysAuthErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
@Component
public class SecurityEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AysAuthErrorResponse aysAuthErrorResponse = new AysAuthErrorResponse(new Date(),"FORBIDDEN","AUTH ERROR",false);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper o = new ObjectMapper();
        o.writeValue(outputStream,aysAuthErrorResponse);
        outputStream.flush();
    }
}
