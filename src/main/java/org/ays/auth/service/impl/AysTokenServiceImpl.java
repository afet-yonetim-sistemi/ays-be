package org.ays.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.enums.AysUserType;
import org.ays.auth.service.AysTokenService;
import org.ays.auth.util.exception.TokenNotValidException;
import org.ays.common.util.AysListUtil;
import org.ays.common.util.AysRandomUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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

    private final AysTokenConfigurationParameter tokenConfiguration;

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
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
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
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getIssuer())
                .issuedAt(accessTokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claims(claims)
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Verifies and validates the given JWT (JSON Web Token).
     * This method parses the token using the public key from the {@link AysTokenConfigurationParameter},
     * and throws a {@link TokenNotValidException} if the token is not valid due to being malformed, expired or having an invalid signature.
     *
     * @param token The JWT (JSON Web Token) to be verified and validated.
     * @throws TokenNotValidException If the token is not valid due to being malformed, expired or having an invalid signature.
     */
    @Override
    public void verifyAndValidate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(tokenConfiguration.getPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException | ExpiredJwtException | SignatureException exception) {
            throw new TokenNotValidException(token, exception);
        }
    }

    /**
     * Parses the given JWT and returns its claims as a {@link Claims} object.
     *
     * @param token the JWT string to parse
     * @return the parsed JWT claims as a {@link Claims} object
     */
    @Override
    public Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(tokenConfiguration.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the authentication object {@link UsernamePasswordAuthenticationToken} based on the provided token.
     * This method parses the token using the public key from the {@link AysTokenConfigurationParameter} and extracts the necessary information from the token claims,
     * such as user type and roles, to construct the authentication object.
     *
     * @param token The token string used for authentication.
     * @return The constructed {@link UsernamePasswordAuthenticationToken} object.
     */
    @Override
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(tokenConfiguration.getPublicKey())
                .build()
                .parseSignedClaims(token);

        JwsHeader header = claims.getHeader();
        Claims payload = claims.getPayload();

        final Jwt jwt = new Jwt(
                token,
                payload.getIssuedAt().toInstant(),
                payload.getExpiration().toInstant(),
                Map.of(
                        AysTokenClaims.TYPE.getValue(), header.getType(),
                        AysTokenClaims.ALGORITHM.getValue(), header.getAlgorithm()
                ),
                payload
        );

        final AysUserType userType = AysUserType.valueOf(payload.get(AysTokenClaims.USER_TYPE.getValue()).toString());

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userType.name()));

        if (userType == AysUserType.USER) {
            final List<String> roles = AysListUtil.to(payload.get(AysTokenClaims.ROLES.getValue()), String.class);
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        }

        return UsernamePasswordAuthenticationToken.authenticated(jwt, null, authorities);
    }
}
