package org.ays.auth.service;

/**
 * Provides services for invalidating tokens and checking invalid token state.
 */
public interface AysInvalidTokenService {

    /**
     * Invalidates both access and refresh token IDs.
     *
     * @param accessTokenId  the access token ID to invalidate
     * @param refreshTokenId the refresh token ID to invalidate
     */
    void invalidateTokens(String accessTokenId, String refreshTokenId);

    /**
     * Checks whether the given token ID is invalid.
     *
     * @param tokenId the token ID to validate
     */
    void checkForInvalidityOfToken(String tokenId);

}
