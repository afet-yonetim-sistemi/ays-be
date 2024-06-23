package org.ays.auth.service;

import io.jsonwebtoken.Claims;
import org.ays.auth.model.AysToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Provides services for generating, validating, and processing JSON Web Tokens (JWTs) in the AYS system.
 * This interface defines methods for generating tokens with claims, verifying and validating tokens,
 * extracting payloads, and obtaining authentication information from tokens.
 * <p>
 * Implementations should ensure secure handling of JWTs and compliance with AYS security requirements.
 */
public interface AysTokenService {

    /**
     * Generates a new {@link AysToken} based on the provided claims.
     * This method creates a JWT containing the specified claims.
     *
     * @param claims the {@link Claims} to be included in the JWT
     * @return an {@link AysToken} containing the generated JWT
     */
    AysToken generate(Claims claims);

    /**
     * Generates a new {@link AysToken} with an existing refresh token and specified claims.
     * This method creates a JWT with the given claims and associates it with the provided refresh token.
     *
     * @param claims       the {@link Claims} to be included in the JWT
     * @param refreshToken the refresh token to be associated with the new JWT
     * @return an {@link AysToken} containing the generated JWT and the associated refresh token
     */
    AysToken generate(Claims claims, String refreshToken);

    /**
     * Verifies and validates the given JWT.
     * This method checks the JWT's signature, expiration, and other validity criteria.
     *
     * @param jwt the JWT as a {@link String} to be verified and validated
     */
    void verifyAndValidate(String jwt);

    /**
     * Extracts the payload (claims) from the given JWT.
     * This method returns the claims contained within the JWT.
     *
     * @param jwt the JWT as a {@link String} from which the payload will be extracted
     * @return the {@link Claims} extracted from the JWT
     */
    Claims getPayload(String jwt);

    /**
     * Retrieves an authentication object from the given JWT.
     * This method creates an {@link UsernamePasswordAuthenticationToken} based on the claims in the JWT.
     *
     * @param jwt the JWT as a {@link String} from which the authentication will be extracted
     * @return a {@link UsernamePasswordAuthenticationToken} containing authentication information
     */
    UsernamePasswordAuthenticationToken getAuthentication(String jwt);

}
