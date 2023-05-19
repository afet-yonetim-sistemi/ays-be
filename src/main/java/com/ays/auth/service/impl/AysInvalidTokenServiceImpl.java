package com.ays.auth.service.impl;

import com.ays.auth.model.entity.AysInvalidTokenEntity;
import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.util.exception.TokenAlreadyInvalidatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * AysInvalidTokenServiceImpl is an implementation of the AysInvalidTokenService interface that provides
 * functionality for managing and checking the validity of tokens.
 */
@Service
@RequiredArgsConstructor
class AysInvalidTokenServiceImpl implements AysInvalidTokenService {

    private final AysInvalidTokenRepository invalidTokenRepository;

    /**
     * Invalidates the specified token IDs by saving them as invalid tokens in the repository.
     *
     * @param tokenIds a set of token IDs to invalidate
     */
    @Override
    public void invalidateTokens(final Set<String> tokenIds) {
        final Set<AysInvalidTokenEntity> invalidTokenEntities = tokenIds.stream()
                .map(tokenId -> AysInvalidTokenEntity.builder()
                        .tokenId(tokenId)
                        .build()
                )
                .collect(Collectors.toSet());

        invalidTokenRepository.saveAll(invalidTokenEntities);
    }

    /**
     * Checks the validity of a token by searching for it in the repository.
     * If the token is found and marked as invalid, a TokenAlreadyInvalidatedException is thrown.
     *
     * @param tokenId the ID of the token to check for invalidity
     * @throws TokenAlreadyInvalidatedException if the token is already marked as invalid
     */
    @Override
    public void checkForInvalidityOfToken(final String tokenId) {
        final boolean isTokenInvalid = invalidTokenRepository.findByTokenId(tokenId).isPresent();
        if (isTokenInvalid) {
            throw new TokenAlreadyInvalidatedException(tokenId);
        }
    }

}
