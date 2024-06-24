package org.ays.auth.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import org.ays.common.util.AysRandomUtil;

public class AysTokenBuilder {

    public static Claims addValidTokenClaims(Claims claims) {
        ClaimsBuilder claimsBuilder = Jwts.claims()
                .id(AysRandomUtil.generateUUID());

        claims.forEach(claimsBuilder::add);

        return claimsBuilder.build();
    }

}
