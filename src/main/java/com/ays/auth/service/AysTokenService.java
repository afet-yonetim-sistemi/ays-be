package com.ays.auth.service;

import com.ays.auth.model.AysToken;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

/**
 * AYS Token service to handle with JWT processes
 */
public interface AysTokenService {

    /**
     * Generate AysToken with Claims.
     *
     * @param claims Map Object
     */
    AysToken generate(Map<String, Object> claims);

    /**
     * Generate AysToken with Claims for refresh token.
     *
     * @param claims       Map Object
     * @param refreshToken refreshToken text
     */
    AysToken generate(Map<String, Object> claims, String refreshToken);

    /**
     * Verify and Validate JWT
     *
     * @param jwt jwt text
     */
    void verifyAndValidate(String jwt);

    /**
     * Get Claims from JWT
     *
     * @param jwt jwt text
     */
    Claims getClaims(String jwt);

    /**
     * Get Authentication Info from JWT
     *
     * @param jwt jwt text
     */
    UsernamePasswordAuthenticationToken getAuthentication(String jwt);

}
