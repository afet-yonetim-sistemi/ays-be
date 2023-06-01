package com.ays.auth.filter;
import com.ays.common.util.exception.model.AysError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.DateFormat;

@Component
public class AysBearerTokenControlFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            response.setStatus(401);
            AysError aysError = AysError.builder()
                    .header(AysError.Header.AUTH_ERROR.getName())
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .isSuccess(false)
                    .build();
            ObjectMapper obj = new ObjectMapper();
            obj.registerModule(new JavaTimeModule());
            byte[] res =obj.writer(DateFormat.getDateInstance()).writeValueAsBytes(aysError);
            response.getOutputStream().write(res);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
