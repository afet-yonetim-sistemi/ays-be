package org.ays.auth.port;

/**
 * Port interface for saving invalid token information.
 */
public interface AysInvalidTokenSavePort {

    /**
     * Persists access and refresh token IDs as invalid tokens.
     *
     * @param accessTokenId  The access token ID to invalidate.
     * @param refreshTokenId The refresh token ID to invalidate.
     */
    void saveAll(String accessTokenId, String refreshTokenId);

}
