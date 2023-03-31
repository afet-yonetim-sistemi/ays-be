package com.ays.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class HttpServletRequestWrapper {

    public static String getToken(HttpServletRequest httpServletRequest) {

        final String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken.contains("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }

        return null;
    }
}
