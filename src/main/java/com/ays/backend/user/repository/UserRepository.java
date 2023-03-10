package com.ays.backend.user.repository;

import java.util.Optional;

import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository performing DB layer operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByIdAndStatusNot(Long id, UserStatus status);
}
