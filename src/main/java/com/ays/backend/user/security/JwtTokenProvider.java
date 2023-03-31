package com.ays.backend.user.security;


import com.ays.backend.user.model.Token;
import io.jsonwebtoken.*;
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

    @Value("${ays.token.expires-in}")
    private Long TOKEN_EXPIRE_IN;

    public Token generateJwtToken(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        final Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final long currentTimeMillis = System.currentTimeMillis();
        final Date accessTokenExpireIn = new Date(currentTimeMillis + TOKEN_EXPIRE_IN);

        final JwtBuilder token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS512, APP_SECRET); // TODO : SignatureAlgorithm and APP_SECRET should be read from the database

        final String accessToken = token
                .setId(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("username", userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(accessTokenExpireIn)
                .compact();

        final String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("username", userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(accessTokenExpireIn)
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpireIn(currentTimeMillis + TOKEN_EXPIRE_IN)
                .refreshToken(refreshToken)
                .build();
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
