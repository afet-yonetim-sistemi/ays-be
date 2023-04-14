package com.ays.admin_user.repository;

import com.ays.admin_user.model.entity.AdminUserRegisterVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AdminUserRegisterVerificationRepository performing DB layer operations.
 */
public interface AdminUserRegisterVerificationRepository extends JpaRepository<AdminUserRegisterVerificationEntity, String> {
}
