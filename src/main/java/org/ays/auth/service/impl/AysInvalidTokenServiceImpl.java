package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysInvalidToken;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.util.exception.AysTokenAlreadyInvalidatedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AysInvalidTokenService} interface for managing invalid tokens within the application.
 * <p>
 * This service class provides methods to invalidate tokens and check if a token has already been invalidated.
 * It uses ports to read and save invalid tokens, ensuring that token management operations are handled efficiently.
 * </p>
 */
@Service
@RequiredArgsConstructor
class AysInvalidTokenServiceImpl implements AysInvalidTokenService {

    private final AysInvalidTokenReadPort invalidTokenReadPort;
    private final AysInvalidTokenSavePort invalidTokenSavePort;

    /**
     * Invalidates multiple tokens by saving them as invalid tokens in the system.
     * <p>
     * This method converts each token ID into an {@link AysInvalidToken} object and saves them using
     * the {@link AysInvalidTokenSavePort}.
     * </p>
     *
     * @param tokenIds the set of token IDs to invalidate
     */
    @Override
    @Transactional
    public void invalidateTokens(final Set<String> tokenIds) {
        final Set<AysInvalidToken> invalidTokens = tokenIds.stream()
                .map(tokenId -> AysInvalidToken.builder()
                        .tokenId(tokenId)
                        .build()
                )
                .collect(Collectors.toSet());

        invalidTokenSavePort.saveAll(invalidTokens);
    }

    /**
     * Checks if a token has already been invalidated.
     * <p>
     * This method queries the {@link AysInvalidTokenReadPort} to determine if the specified token ID
     * exists as an invalidated token. If it does, an {@link AysTokenAlreadyInvalidatedException} is thrown.
     * </p>
     *
     * @param tokenId the token ID to check for invalidity
     * @throws AysTokenAlreadyInvalidatedException if the token has already been invalidated
     */
    @Override
    @Transactional(readOnly = true)
    public void checkForInvalidityOfToken(final String tokenId) {
        final boolean isTokenInvalid = invalidTokenReadPort.findByTokenId(tokenId).isPresent();
        if (isTokenInvalid) {
            throw new AysTokenAlreadyInvalidatedException(tokenId);
        }
    }

}
