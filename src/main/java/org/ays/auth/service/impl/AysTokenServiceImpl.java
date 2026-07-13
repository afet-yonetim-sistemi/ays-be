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
import org.ays.auth.model.enums.AysTokenVariant;
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
import java.util.Optional;

/**
 * Implementation of {@link AysTokenService} for handling JSON Web Token (JWT) generation, validation, and processing.
 * <p>
 * This service provides the following functionalities:
 * <ul>
 *     <li>Token Generation: Creates access and refresh tokens with configurable expiration times and claims.</li>
 *     <li>Token Verification: Validates JWT signatures, structure, expiration, and token variant.</li>
 *     <li>Token Parsing: Extracts payload claims from valid JWTs.</li>
 *     <li>Authentication Extraction: Constructs authentication objects from token claims for Spring Security integration.</li>
 * </ul>
 * <p>
 * All generated tokens include a token variant ({@link AysTokenVariant#ACCESS} or {@link AysTokenVariant#REFRESH})
 * in their header for differentiation and validation. Tokens are signed using the RS256 algorithm with a configured private key.
 * <p>
 * This implementation follows the AYS system's security requirements and is configured via {@link AysApplicationConfigurationParameter}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class AysTokenServiceImpl implements AysTokenService {

    private final AysApplicationConfigurationParameter tokenConfiguration;

    private static final String TOKEN_TYPE = "JWT";

    /**
     * Generates an access token and a refresh token based on the provided claims.
     * The access token is marked with {@link AysTokenVariant#ACCESS} and the refresh token with {@link AysTokenVariant#REFRESH}.
     *
     * @param claims The claims to be included in the tokens.
     * @return AysToken object containing the access token and refresh token with their respective variants.
     */
    @Override
    public AysToken generate(final Claims claims) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute()
        );

        final String accessToken = this.initializeTokenBuilder(currentTimeMillis, AysTokenVariant.ACCESS)
                .id(AysRandomUtil.generateUUID())
                .expiration(accessTokenExpiresAt)
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireMinute()
        );
        final String refreshToken = this.initializeTokenBuilder(currentTimeMillis, AysTokenVariant.REFRESH)
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
     * Generates an access token based on the provided claims and an existing refresh token.
     * The generated access token is marked with {@link AysTokenVariant#ACCESS}.
     *
     * @param claims       The claims to be included in the access token.
     * @param refreshToken The existing refresh token to be associated with the new access token.
     * @return AysToken object containing the generated access token and the provided refresh token.
     */
    @Override
    public AysToken generate(final Claims claims, final String refreshToken) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute()
        );

        final String accessToken = this.initializeTokenBuilder(currentTimeMillis, AysTokenVariant.ACCESS)
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
     * Initializes a JwtBuilder for creating a JSON Web Token (JWT) with the specified current time and token variant.
     *
     * @param currentTimeMillis The current time in milliseconds to be used as the "issued at" claim.
     * @param variant           The {@link AysTokenVariant} to be included in the token header.
     * @return JwtBuilder instance configured with default and provided settings.
     * <p>
     * The JWT will have the following claims set:
     * - Header with the token type set to JWT and variant claim.
     * - Issuer claim set to the configured issuer from the token configuration.
     * - Issued At (iat) claim set to the specified current time.
     * - Signature configured with the private key from the token configuration.
     */
    private JwtBuilder initializeTokenBuilder(final long currentTimeMillis, final AysTokenVariant variant) {
        return Jwts.builder()
                .header()
                .type(TOKEN_TYPE)
                .add(AysTokenClaims.VARIANT.getValue(), variant)
                .and()
                .issuer(tokenConfiguration.getTokenIssuer())
                .issuedAt(new Date(currentTimeMillis))
                .signWith(tokenConfiguration.getTokenPrivateKey());
    }


    /**
     * Verifies and validates the given JWT (JSON Web Token).
     * This method parses the token using the public key from the {@link AysApplicationConfigurationParameter},
     * validates the token header, and verifies that the token variant matches the expected variant.
     * Throws a {@link AysTokenNotValidException} if the token is not valid due to being malformed, expired, having an invalid signature, or not matching the expected token variant.
     *
     * @param token   The JWT (JSON Web Token) to be verified and validated.
     * @param variant The expected {@link AysTokenVariant} to validate against the token's variant claim.
     * @throws AysTokenNotValidException If the token is not valid due to being malformed, expired, having an invalid signature, or variant mismatch.
     */
    @Override
    public void verifyAndValidate(final String token, final AysTokenVariant variant) {
        try {
            final Jws<Claims> claims = Jwts.parser()
                    .verifyWith(tokenConfiguration.getTokenPublicKey())
                    .build()
                    .parseSignedClaims(token);

            final JwsHeader header = claims.getHeader();

            if (!TOKEN_TYPE.equals(header.getType())) {
                throw new RequiredTypeException(token);
            }

            if (!OAuth2AccessToken.TokenType.BEARER.getValue().equals(header.getType())) {
                throw new RequiredTypeException(token);
            }

            if (!Jwts.SIG.RS256.getId().equals(header.getAlgorithm())) {
                throw new SignatureException(token);
            }

            final AysTokenVariant variantFromToken = Optional
                    .ofNullable(header.get(AysTokenClaims.VARIANT.getValue()))
                    .map(String::valueOf)
                    .map(AysTokenVariant::valueOf)
                    .orElse(null);
            if (variant != variantFromToken) {
                throw new RequiredTypeException(token);
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
                .verifyWith(tokenConfiguration.getTokenPublicKey())
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
                .verifyWith(tokenConfiguration.getTokenPublicKey())
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
