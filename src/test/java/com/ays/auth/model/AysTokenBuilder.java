package com.ays.auth.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.HashMap;
import java.util.Map;

public class AysTokenBuilder {

    public static Claims getValidClaims(String username) {
        Map<String, Object> mockClaimsMap = new HashMap<>();
        mockClaimsMap.put("username", username);
        return Jwts.claims(mockClaimsMap);
    }

}