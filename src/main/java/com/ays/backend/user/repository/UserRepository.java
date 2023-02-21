package com.ays.backend.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.ays.backend.user.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository performing DB layer operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByUserUUID(UUID userId);
}
