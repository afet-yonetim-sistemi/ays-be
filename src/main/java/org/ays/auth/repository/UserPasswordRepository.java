package org.ays.auth.repository;

import org.ays.auth.model.entity.UserEntityV2;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link UserEntityV2.PasswordEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link UserEntityV2.PasswordEntity} objects.
 */
public interface UserPasswordRepository extends JpaRepository<UserEntityV2.PasswordEntity, Long> {
}
