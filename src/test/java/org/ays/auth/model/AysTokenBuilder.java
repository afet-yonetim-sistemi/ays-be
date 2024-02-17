package org.ays.auth.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.common.util.AysRandomUtil;

import java.util.HashMap;
import java.util.Map;

public class AysTokenBuilder {

    public static Claims getValidClaims(String userId, String username) {
        Map<String, Object> mockClaimsMap = new HashMap<>();
        mockClaimsMap.put(AysTokenClaims.JWT_ID.getValue(), AysRandomUtil.generateUUID());
        mockClaimsMap.put(AysTokenClaims.USER_ID.getValue(), userId);
        mockClaimsMap.put(AysTokenClaims.USERNAME.getValue(), username);
        return Jwts.claims(mockClaimsMap);
    }

}
