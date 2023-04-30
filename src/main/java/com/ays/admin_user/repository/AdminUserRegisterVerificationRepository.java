package com.ays.admin_user.repository;

import com.ays.admin_user.model.entity.AdminUserRegisterVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * An interface for the repository managing the verification of admin user registration process in the system.
 * Extends the JpaRepository interface, specifying the entity type as {@link AdminUserRegisterVerificationEntity} and the ID type as {@link String}.
 */
public interface AdminUserRegisterVerificationRepository extends JpaRepository<AdminUserRegisterVerificationEntity, String> {
}
