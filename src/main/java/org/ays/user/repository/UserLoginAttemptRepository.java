package org.ays.user.repository;

import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginAttemptRepository extends JpaRepository<UserLoginAttemptEntity, String> {

    /**
     * Retrieves a user's login attempt based on their user ID.
     *
     * @param userId The unique identifier of the user.
     * @return An {@link UserLoginAttemptEntity} object representing the user's login attempt.
     */
    UserLoginAttemptEntity findByUserId(final String userId);

}
