package org.ays.admin_user.repository;

import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * An interface for the repository managing the verification of admin user registration process in the system.
 * Extends the JpaRepository interface, specifying the entity type as {@link AdminUserRegisterApplicationEntity} and the ID type as {@link String}.
 */
public interface AdminUserRegisterApplicationRepository extends JpaRepository<AdminUserRegisterApplicationEntity, String>, JpaSpecificationExecutor<AdminUserRegisterApplicationEntity> {
}
