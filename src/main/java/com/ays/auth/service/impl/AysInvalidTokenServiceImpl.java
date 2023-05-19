package com.ays.auth.service.impl;

import com.ays.auth.model.entity.AysInvalidTokenEntity;
import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.util.exception.TokenAlreadyInvalidatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AysInvalidTokenServiceImpl implements AysInvalidTokenService {

    private final AysInvalidTokenRepository invalidTokenRepository;

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

    @Override
    public void checkForInvalidityOfToken(final String tokenId) {
        final boolean isTokenInvalid = invalidTokenRepository.findByTokenId(tokenId).isPresent();
        if (isTokenInvalid) {
            throw new TokenAlreadyInvalidatedException(tokenId);
        }
    }

}
