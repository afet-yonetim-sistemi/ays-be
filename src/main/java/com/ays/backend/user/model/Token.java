package com.ays.backend.user.model;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Set;

@Getter
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpireIn;
    private String refreshToken;

    public static JwtBuilder initializeJwtBuilder(UserDetails userDetails,
                                                  Set<String> roles,
                                                  long currentTimeMillis,
                                                  String appSecret) {

        return Jwts.builder()
                .claim("roles", roles)
                .claim("username", userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeMillis))
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS512, appSecret); // TODO : SignatureAlgorithm and APP_SECRET should be read from the database
    }

}
