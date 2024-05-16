package org.ays.admin_user.repository;

import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * An interface for the repository managing the verification of admin registration process in the system.
 * Extends the JpaRepository interface, specifying the entity type as {@link AdminRegisterApplicationEntity} and the ID type as {@link String}.
 */
public interface AdminRegisterApplicationRepository extends JpaRepository<AdminRegisterApplicationEntity, String>, JpaSpecificationExecutor<AdminRegisterApplicationEntity> {
}
