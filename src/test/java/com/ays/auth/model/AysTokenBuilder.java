package com.ays.auth.model;

import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.common.util.AysRandomUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.HashMap;
import java.util.Map;

public class AysTokenBuilder {

    public static Claims getValidClaims(String username) {
        Map<String, Object> mockClaimsMap = new HashMap<>();
        mockClaimsMap.put(AysTokenClaims.JWT_ID.getValue(), AysRandomUtil.generateUUID());
        mockClaimsMap.put(AysTokenClaims.USERNAME.getValue(), username);
        return Jwts.claims(mockClaimsMap);
    }

}
