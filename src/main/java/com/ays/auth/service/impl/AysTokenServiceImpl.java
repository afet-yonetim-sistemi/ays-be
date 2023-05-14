package com.ays.auth.service.impl;

import com.ays.auth.config.AysTokenConfiguration;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.TokenNotValidException;
import com.ays.common.util.AysListUtil;
import com.ays.common.util.AysRandomUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AYS Token service to handle with JWT processes
 */
@Slf4j
@Service
@RequiredArgsConstructor
class AysTokenServiceImpl implements AysTokenService {

    private final AysTokenConfiguration tokenConfiguration;

    /**
     * Generates an access token and a refresh token based on the provided claims.
     *
     * @param claims The claims to be included in the tokens.
     * @return AysToken object containing the access token and refresh token.
     */
    @Override
    public AysToken generate(final Map<String, Object> claims) {
        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());
        final String accessToken = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .claim(AysTokenClaims.USERNAME.getValue(), claims.get(AysTokenClaims.USERNAME.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Generates an access token based on the provided claims and refresh token.
     *
     * @param claims       The claims to be included in the access token.
     * @param refreshToken The refresh token.
     * @return AysToken object containing the generated access token and refresh token.
     */
    @Override
    public AysToken generate(final Map<String, Object> claims, final String refreshToken) {

        final long currentTimeMillis = System.currentTimeMillis();
        final Date accessTokenIssuedAt = new Date(currentTimeMillis);
        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());

        final String accessToken = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(accessTokenIssuedAt)
                .setExpiration(accessTokenExpiresAt)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .addClaims(claims)
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Verifies and validates the given JWT (JSON Web Token).
     * This method parses the token using the public key from the {@link AysTokenConfiguration},
     * and throws a {@link TokenNotValidException} if the token is not valid due to being malformed, expired or having an invalid signature.
     *
     * @param jwt The JWT (JSON Web Token) to be verified and validated.
     * @throws TokenNotValidException If the token is not valid due to being malformed, expired or having an invalid signature.
     */
    @Override
    public void verifyAndValidate(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(tokenConfiguration.getPublicKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (MalformedJwtException | ExpiredJwtException | SignatureException exception) {
            throw new TokenNotValidException(jwt, exception);
        }
    }

    /**
     * Parses the given JWT and returns its claims as a {@link Claims} object.
     *
     * @param jwt the JWT string to parse
     * @return the parsed JWT claims as a {@link Claims} object
     */
    @Override
    public Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenConfiguration.getPublicKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * Retrieves the authentication object {@link UsernamePasswordAuthenticationToken} based on the provided token.
     * This method parses the token using the public key from the {@link AysTokenConfiguration} and extracts the necessary information from the token claims,
     * such as user type and roles, to construct the authentication object.
     *
     * @param token The token string used for authentication.
     * @return The constructed {@link UsernamePasswordAuthenticationToken} object.
     */
    @Override
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenConfiguration.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        final Jwt jwt = new Jwt(
                token,
                Instant.ofEpochSecond(((Double) claims.get(AysTokenClaims.ISSUED_AT.getValue())).intValue()),
                Instant.ofEpochSecond(((Double) claims.get(AysTokenClaims.EXPIRES_AT.getValue())).intValue()),
                Map.of(AysTokenClaims.ALGORITHM.getValue(), SignatureAlgorithm.RS512.getValue()),
                claims
        );

        final AysUserType userType = AysUserType.valueOf(claims.get(AysTokenClaims.USER_TYPE.getValue()).toString());

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        switch (userType) {
            case ADMIN -> authorities.add(new SimpleGrantedAuthority("ADMIN"));
            case USER -> {
                final List<String> roles = AysListUtil.to(claims.get(AysTokenClaims.ROLES.getValue()), String.class);
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            }
        }

        return UsernamePasswordAuthenticationToken.authenticated(jwt, null, authorities);
    }
}
