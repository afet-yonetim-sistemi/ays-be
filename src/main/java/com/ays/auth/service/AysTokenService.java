package com.ays.auth.service;

import com.ays.auth.model.AysToken;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

public interface AysTokenService {

    AysToken generate(Map<String, Object> claims);

    AysToken generate(Map<String, Object> claims, String refreshToken);

    void verifyAndValidate(String jwt);

    Claims getClaims(String jwt);

    UsernamePasswordAuthenticationToken getAuthentication(String jwt);

}
