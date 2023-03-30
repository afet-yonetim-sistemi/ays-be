package com.ays.backend.user.repository;

import com.ays.backend.user.model.entities.UserEntity;
import com.ays.backend.user.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository performing DB layer operations.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByIdAndStatusNot(Long id, UserStatus status);
}
