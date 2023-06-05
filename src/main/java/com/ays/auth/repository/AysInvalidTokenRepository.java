package com.ays.auth.repository;

import com.ays.auth.model.entity.AysInvalidTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * AysInvalidTokenRepository is a repository interface that provides methods for accessing and managing invalid token entities.
 */
public interface AysInvalidTokenRepository extends JpaRepository<AysInvalidTokenEntity, Long> {

    /**
     * Finds an invalid token entity by its token ID.
     *
     * @param tokenId the ID of the token to search for
     * @return an Optional containing the found invalid token entity, or an empty Optional if not found
     */
    Optional<AysInvalidTokenEntity> findByTokenId(String tokenId);

}
