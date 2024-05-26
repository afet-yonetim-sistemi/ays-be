package org.ays.user.repository;

import org.ays.user.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link RoleEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@code RoleEntity} objects.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    /**
     * Finds a {@link RoleEntity} by the given institution ID.
     *
     * @param institutionId the ID of the institution
     * @return an {@link Optional} containing the {@link RoleEntity} if found, or empty if not found
     */
    Optional<RoleEntity> findByInstitutionId(String institutionId);

}