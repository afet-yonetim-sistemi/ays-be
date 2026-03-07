package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.exception.AysTokenAlreadyInvalidatedException;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.auth.service.AysInvalidTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AysInvalidTokenService} for invalidating tokens and validating invalid token state.
 * <p>
 * This service coordinates read and save operations through invalid token ports and throws
 * {@link AysTokenAlreadyInvalidatedException} when an already invalidated token is detected.
 * </p>
 */
@Service
@RequiredArgsConstructor
class AysInvalidTokenServiceImpl implements AysInvalidTokenService {

    private final AysInvalidTokenReadPort invalidTokenReadPort;
    private final AysInvalidTokenSavePort invalidTokenSavePort;

    /**
     * Invalidates both access and refresh token IDs.
     *
     * @param accessTokenId  the access token ID to invalidate
     * @param refreshTokenId the refresh token ID to invalidate
     */
    @Override
    @Transactional
    public void invalidateTokens(final String accessTokenId,
                                 final String refreshTokenId) {

        invalidTokenSavePort.saveAll(accessTokenId, refreshTokenId);
    }

    /**
     * Checks if a token has already been invalidated.
     *
     * @param tokenId the token ID to check
     * @throws AysTokenAlreadyInvalidatedException if the token is already invalidated
     */
    @Override
    public void checkForInvalidityOfToken(final String tokenId) {
        final boolean isTokenInvalid = invalidTokenReadPort.exists(tokenId);
        if (isTokenInvalid) {
            throw new AysTokenAlreadyInvalidatedException(tokenId);
        }
    }

}
