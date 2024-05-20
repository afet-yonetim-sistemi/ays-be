package org.ays.user.repository;

import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The {@link UserLoginAttemptRepository} interface defines a repository for managing and accessing user login attempts
 * on the authentication side of the application. It extends the JpaRepository interface, providing CRUD operations for the
 * {@link UserLoginAttemptEntity} entities in the database.
 * <p>
 * This repository includes methods for querying login attempts based on user identifiers, which is useful for tracking and analyzing login activities.
 */
public interface UserLoginAttemptRepository extends JpaRepository<UserLoginAttemptEntity, String> {

    /**
     * Retrieves a user's login attempt based on their user ID.
     *
     * @param userId The unique identifier of the user.
     * @return An {@link UserLoginAttemptEntity} object representing the user's login attempt.
     */
    Optional<UserLoginAttemptEntity> findByUserId(String userId);

}
