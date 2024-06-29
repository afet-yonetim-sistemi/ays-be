package org.ays.auth.repository;

import org.ays.auth.model.entity.AysPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing {@link AysPermissionEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link AysPermissionEntity} objects.
 */
public interface AysPermissionRepository extends JpaRepository<AysPermissionEntity, String> {

    /**
     * Retrieves all permissions that are not marked as super.
     *
     * @return a list of {@link AysPermissionEntity} objects that are not marked as super
     */
    List<AysPermissionEntity> findAllByIsSuperFalse();

    /**
     * Retrieves all permissions with the specified IDs.
     *
     * @param ids the set of permission IDs to retrieve
     * @return a list of {@link AysPermissionEntity} objects with the specified IDs
     */
    List<AysPermissionEntity> findAllByIdIn(Set<String> ids);

}
