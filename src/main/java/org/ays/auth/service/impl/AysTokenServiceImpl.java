package org.ays.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.exception.AysTokenNotValidException;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.service.AysTokenService;
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

    private final AysApplicationConfigurationParameter tokenConfiguration;

    /**
     * Generates an access token and a refresh token based on the provided claims.
     *
     * @param claims The claims to be included in the tokens.
     * @return AysToken object containing the access token and refresh token.
     */
    @Override
    public AysToken generate(final Claims claims) {

        final long currentTimeMillis = System.currentTimeMillis();

        final JwtBuilder tokenBuilder = this.initializeTokenBuilder(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute()
        );

        final String accessToken = tokenBuilder
                .id(AysRandomUtil.generateUUID())
                .expiration(accessTokenExpiresAt)
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(
                new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay()
        );
        final String refreshToken = tokenBuilder
                .id(AysRandomUtil.generateUUID())
                .expiration(refreshTokenExpiresAt)
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
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
    public AysToken generate(final Claims claims, final String refreshToken) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute()
        );

        final String accessToken = this.initializeTokenBuilder(currentTimeMillis)
                .id(AysRandomUtil.generateUUID())
                .expiration(accessTokenExpiresAt)
                .claims(claims)
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Initializes a JwtBuilder for creating a JSON Web Token (JWT) with the specified current time.
     *
     * @param currentTimeMillis The current time in milliseconds to be used as the "issued at" claim.
     * @return JwtBuilder instance configured with default and provided settings.
     * <p>
     * The JWT will have the following claims set:
     * - Header with the token type set to Bearer.
     * - Issuer claim set to the configured issuer from the token configuration.
     * - Issued At (iat) claim set to the specified current time.
     * - Signature configured with the private key from the token configuration.
     */
    private JwtBuilder initializeTokenBuilder(long currentTimeMillis) {
        return Jwts.builder()
                .header()
                .type(OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .issuer(tokenConfiguration.getTokenIssuer())
                .issuedAt(new Date(currentTimeMillis))
                .signWith(tokenConfiguration.getTokenPrivateKey());
    }


    /**
     * Verifies and validates the given JWT (JSON Web Token).
     * This method parses the token using the public key from the {@link AysApplicationConfigurationParameter},
     * and throws a {@link AysTokenNotValidException} if the token is not valid due to being malformed, expired or having an invalid signature.
     *
     * @param token The JWT (JSON Web Token) to be verified and validated.
     * @throws AysTokenNotValidException If the token is not valid due to being malformed, expired or having an invalid signature.
     */
    @Override
    public void verifyAndValidate(String token) {
        try {
            final Jws<Claims> claims = Jwts.parser()
                    .verifyWith(tokenConfiguration.getPublicKey())
                    .build()
                    .parseSignedClaims(token);

            final JwsHeader header = claims.getHeader();
            if (!OAuth2AccessToken.TokenType.BEARER.getValue().equals(header.getType())) {
                throw new RequiredTypeException(token);
            }

            if (!Jwts.SIG.RS256.getId().equals(header.getAlgorithm())) {
                throw new SignatureException(token);
            }

        } catch (MalformedJwtException | ExpiredJwtException | SignatureException | RequiredTypeException exception) {
            throw new AysTokenNotValidException(token, exception);
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
     * This method parses the token using the public key from the {@link AysApplicationConfigurationParameter} and extracts the necessary information from the token claims,
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

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        final List<String> permissions = AysListUtil.to(payload.get(AysTokenClaims.USER_PERMISSIONS.getValue()), String.class);
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));

        return UsernamePasswordAuthenticationToken.authenticated(jwt, null, authorities);
    }
}
