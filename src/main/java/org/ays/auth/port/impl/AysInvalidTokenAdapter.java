package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysInvalidToken;
import org.ays.auth.model.entity.AysInvalidTokenEntity;
import org.ays.auth.model.mapper.AysInvalidTokenEntityToDomainMapper;
import org.ays.auth.model.mapper.AysInvalidTokenToEntityMapper;
import org.ays.auth.port.AysInvalidTokenDeletePort;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Adapter class that implements read, save, and delete ports for handling {@link AysInvalidToken} entities.
 * This component interacts with the {@link AysInvalidTokenRepository} to perform database operations.
 */
@Component
@RequiredArgsConstructor
class AysInvalidTokenAdapter implements AysInvalidTokenReadPort, AysInvalidTokenSavePort, AysInvalidTokenDeletePort {

    private final AysInvalidTokenRepository invalidTokenRepository;


    private final AysInvalidTokenEntityToDomainMapper invalidTokenEntityToDomainMapper = AysInvalidTokenEntityToDomainMapper.initialize();
    private final AysInvalidTokenToEntityMapper invalidTokenToEntityMapper = AysInvalidTokenToEntityMapper.initialize();


    /**
     * Retrieves an {@link AysInvalidToken} by its token ID.
     *
     * @param tokenId The token ID of the {@link AysInvalidToken} to retrieve.
     * @return An {@link Optional} containing the found {@link AysInvalidToken}, or empty if not found.
     */
    @Override
    public Optional<AysInvalidToken> findByTokenId(final String tokenId) {
        final Optional<AysInvalidTokenEntity> invalidTokenEntity = invalidTokenRepository.findByTokenId(tokenId);
        return invalidTokenEntity.map(invalidTokenEntityToDomainMapper::map);
    }

    /**
     * Saves a set of {@link AysInvalidToken} entities to the database.
     *
     * @param invalidTokens The set of {@link AysInvalidToken} entities to save.
     */
    @Override
    public void saveAll(final Set<AysInvalidToken> invalidTokens) {
        final List<AysInvalidTokenEntity> invalidTokenEntities = invalidTokenToEntityMapper.map(invalidTokens);
        invalidTokenRepository.saveAll(invalidTokenEntities);
    }

    /**
     * Deletes all {@link AysInvalidToken} entities from the database that were created before the specified expiration threshold.
     *
     * @param expirationThreshold The timestamp threshold before which {@link AysInvalidToken} entities will be deleted.
     */
    @Override
    public void deleteAllByCreatedAtBefore(LocalDateTime expirationThreshold) {
        invalidTokenRepository.deleteAllByCreatedAtBefore(expirationThreshold);
    }

}
