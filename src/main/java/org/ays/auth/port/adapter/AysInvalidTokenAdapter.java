package org.ays.auth.port.adapter;

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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter class that implements read, save, and delete ports for handling {@link AysInvalidToken} entities.
 * This component interacts with the {@link AysInvalidTokenRepository} to perform database operations.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
     * Persists a set of invalid tokens by mapping domain objects to entities, saving them in batch,
     * and then mapping the saved entities back to domain objects.
     *
     * @param invalidTokens the set of invalid tokens to be saved
     * @return a set of {@link AysInvalidToken} objects that have been successfully saved
     */
    @Override
    @Transactional
    public Set<AysInvalidToken> saveAll(final Set<AysInvalidToken> invalidTokens) {
        final List<AysInvalidTokenEntity> invalidTokenEntitiesToBeSave = invalidTokenToEntityMapper.map(invalidTokens);
        final List<AysInvalidTokenEntity> invalidTokenEntities = invalidTokenRepository.saveAll(invalidTokenEntitiesToBeSave);
        return invalidTokenEntities.stream()
                .map(invalidTokenEntityToDomainMapper::map)
                .collect(Collectors.toSet());
    }

    /**
     * Deletes all {@link AysInvalidToken} entities from the database that were created before the specified expiration threshold.
     *
     * @param expirationThreshold The timestamp threshold before which {@link AysInvalidToken} entities will be deleted.
     */
    @Override
    @Transactional
    public void deleteAllByCreatedAtBefore(LocalDateTime expirationThreshold) {
        invalidTokenRepository.deleteAllByCreatedAtBefore(expirationThreshold);
    }

}
