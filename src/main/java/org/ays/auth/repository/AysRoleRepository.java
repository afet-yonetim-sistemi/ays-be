package org.ays.auth.repository;

import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing {@link AysRoleEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link AysRoleEntity} objects.
 */
public interface AysRoleRepository extends JpaRepository<AysRoleEntity, String> {

    /**
     * Retrieves all active roles associated with a specific institution.
     *
     * @param institutionId the ID of the institution
     * @param status        the status of the roles to retrieve
     * @return a set of {@link AysRoleEntity} objects matching the institution ID and status
     */
    Set<AysRoleEntity> findAllByInstitutionIdAndStatus(String institutionId, RoleStatus status);

    /**
     * Finds a {@link AysRoleEntity} by the given role name.
     *
     * @param name the name of the role
     * @return an {@link Optional} containing the {@link AysRoleEntity} if found, or empty if not found
     */
    Optional<AysRoleEntity> findByName(String name);

}
