package org.ays.user.repository;

import org.ays.user.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing {@link RoleEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link RoleEntity} objects.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    /**
     * Finds all {@link RoleEntity} instances associated with the given institution ID.
     *
     * @param institutionId the ID of the institution
     * @return a {@link Set} containing all {@link RoleEntity} instances associated with the given institution ID
     */
    Set<RoleEntity> findAllByInstitutionId(String institutionId);

    /**
     * Finds a {@link RoleEntity} by the given role name.
     *
     * @param name the name of the role
     * @return an {@link Optional} containing the {@link RoleEntity} if found, or empty if not found
     */
    Optional<RoleEntity> findByName(String name);

}
