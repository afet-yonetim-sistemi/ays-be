package com.ays.admin_user.repository;

import com.ays.admin_user.model.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * AdminUserRepository performing DB layer operations.
 */
public interface AdminUserRepository extends JpaRepository<AdminUserEntity, String> {

    Optional<AdminUserEntity> findByUsername(String username);

    Optional<AdminUserEntity> findByEmail(String email);

}
