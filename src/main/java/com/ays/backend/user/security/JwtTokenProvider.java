package com.ays.backend.user.security;


import com.ays.backend.user.model.Token;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${ays.token.secret}")
    private String APP_SECRET;

    @Value("${ays.token.access-expire-minute}")
    private Integer ACCESS_TOKEN_EXPIRE_MINUTE;

    @Value("${ays.token.refresh-expire-day}")
    private Integer REFRESH_TOKEN_EXPIRE_DAY;

    public Token generateJwtToken(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        final Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final long currentTimeMillis = System.currentTimeMillis();
        final JwtBuilder jwtBuilder = Token.initializeJwtBuilder(userDetails, roles, currentTimeMillis, APP_SECRET);
        final Date accessTokenExpireIn = DateUtils.addMinutes(new Date(currentTimeMillis), ACCESS_TOKEN_EXPIRE_MINUTE);
        final String accessToken = this.generateAccessToken(accessTokenExpireIn, jwtBuilder);

        final String refreshToken = this.generateRefreshToken(currentTimeMillis, jwtBuilder);

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpireIn(accessTokenExpireIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Token generateJwtToken(Authentication auth, String refreshToken) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        final Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final long currentTimeMillis = System.currentTimeMillis();
        final JwtBuilder jwtBuilder = Token.initializeJwtBuilder(userDetails, roles, currentTimeMillis, APP_SECRET);
        final Date accessTokenExpireIn = DateUtils.addMinutes(new Date(currentTimeMillis), ACCESS_TOKEN_EXPIRE_MINUTE);
        final String accessToken = this.generateAccessToken(accessTokenExpireIn, jwtBuilder);

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpireIn(accessTokenExpireIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }


    private String generateAccessToken(Date accessTokenExpireIn, JwtBuilder jwtBuilder) {
        return jwtBuilder
                .setId(UUID.randomUUID().toString())
                .setExpiration(accessTokenExpireIn)
                .compact();
    }

    private String generateRefreshToken(long currentTimeMillis, JwtBuilder jwtBuilder) {
        final Date refreshTokenExpireIn = DateUtils.addDays(new Date(currentTimeMillis), REFRESH_TOKEN_EXPIRE_DAY);
        return jwtBuilder
                .setId(UUID.randomUUID().toString())
                .setExpiration(refreshTokenExpireIn)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(APP_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            return !this.isTokenExpired(token);
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
