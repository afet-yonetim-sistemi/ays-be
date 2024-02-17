package org.ays.auth.service;

import java.util.Set;

/**
 * AysInvalidTokenService provides an interface for managing invalid tokens.
 */
public interface AysInvalidTokenService {

    /**
     * Invalidates the specified token IDs.
     *
     * @param tokenIds a set of token IDs to invalidate
     */
    void invalidateTokens(final Set<String> tokenIds);

    /**
     * Checks the validity of a token.
     *
     * @param tokenId the ID of the token to check for invalidity
     */
    void checkForInvalidityOfToken(final String tokenId);

}
