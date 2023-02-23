package com.ays.backend.user.repository;

import java.util.Optional;

import com.ays.backend.user.model.entities.UserType;
import com.ays.backend.user.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RoleRepository performing DB layer operations.
 */
public interface RoleRepository extends JpaRepository<UserType, Integer> {
    Optional<UserType> findByName(UserRole name);
}
