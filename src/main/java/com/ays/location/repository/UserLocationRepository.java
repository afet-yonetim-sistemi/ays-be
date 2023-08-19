package com.ays.location.repository;

import com.ays.location.model.entity.UserLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserLocationEntity objects.
 */
public interface UserLocationRepository extends JpaRepository<UserLocationEntity, Long> {

    /**
     * Retrieves the user's location entity based on the provided user ID.
     *
     * @param userId The unique identifier of the user.
     * @return An Optional containing the user's location entity if found, or an empty Optional if not found.
     */
    Optional<UserLocationEntity> findByUserId(String userId);

}
