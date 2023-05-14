package com.ays.user.repository;

import com.ays.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity objects.
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be found
     * @return an optional containing the UserEntity with the given username, or an empty optional if not found
     */
    Optional<UserEntity> findByUsername(String username);

}
