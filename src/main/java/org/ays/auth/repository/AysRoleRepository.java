package org.ays.auth.repository;

import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link AysRoleEntity} entities.
 * <p>
 * This repository provides methods to perform CRUD operations and dynamic queries on
 * {@link AysRoleEntity} objects, including finding roles by institution ID and status, and finding roles by name.
 * </p>
 *
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 */
public interface AysRoleRepository extends JpaRepository<AysRoleEntity, String>, JpaSpecificationExecutor<AysRoleEntity> {

    /**
     * Retrieves all active roles associated with a specific institution.
     *
     * @param institutionId the ID of the institution
     * @param status        the status of the roles to retrieve
     * @return a list of {@link AysRoleEntity} objects matching the institution ID and status
     */
    List<AysRoleEntity> findAllByInstitutionIdAndStatus(String institutionId, AysRoleStatus status);

    /**
     * Finds a {@link AysRoleEntity} by its name and institution ID.
     *
     * @param name          The name of the role.
     * @param institutionId The ID of the institution to which the role belongs.
     * @return An {@link Optional} containing the {@link AysRoleEntity} if found, otherwise empty.
     */
    Optional<AysRoleEntity> findByNameAndInstitutionId(String name, String institutionId);

    /**
     * Checks if any users are assigned to the role identified by the given role ID.
     *
     * @param id The ID of the role to check for assigned users.
     * @return true if there are users assigned to the role, false otherwise.
     */
    @Query("SELECT COUNT(user) > 0 FROM AysUserEntity user JOIN user.roles role WHERE role.id = :id")
    boolean isRoleAssignedToUser(String id);

}
