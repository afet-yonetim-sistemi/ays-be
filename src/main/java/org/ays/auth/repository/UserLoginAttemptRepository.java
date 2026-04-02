package org.ays.auth.repository;

import org.ays.auth.model.entity.AysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persisting {@link AysUserEntity.LoginAttemptEntity} records.
 */
public interface UserLoginAttemptRepository extends JpaRepository<AysUserEntity.LoginAttemptEntity, String> {
}
