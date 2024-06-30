package org.ays.auth.repository;

import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
     * Finds a {@link AysRoleEntity} by the given role name.
     *
     * @param name the name of the role
     * @return an {@link Optional} containing the {@link AysRoleEntity} if found, or empty if not found
     */
    Optional<AysRoleEntity> findByName(String name);

}
