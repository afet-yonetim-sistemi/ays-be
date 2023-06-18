package com.ays.user.repository;

import com.ays.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity objects.
 */
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be found
     * @return an optional containing the UserEntity with the given username, or an empty optional if not found
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Retrieves an optional UserEntity based on the provided user ID and institution ID.
     *
     * @param id            The ID of the user to retrieve.
     * @param institutionId The ID of the institution associated with the user.
     * @return An optional UserEntity that matches the specified ID and institution ID, or an empty optional if not found.
     */
    Optional<UserEntity> findByIdAndInstitutionId(String id, String institutionId);

}
