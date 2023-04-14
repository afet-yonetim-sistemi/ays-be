package com.ays.user.repository;

import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository performing DB layer operations.
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByIdAndStatusNot(Long id, UserStatus status);
}
