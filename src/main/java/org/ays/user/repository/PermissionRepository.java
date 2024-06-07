package org.ays.user.repository;

import org.ays.user.model.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Repository interface for managing {@link PermissionEntity} instances.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link PermissionEntity} objects.
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    /**
     * Retrieves all permissions that are not marked as super.
     *
     * @return a set of {@link PermissionEntity} objects that are not marked as super
     */
    Set<PermissionEntity> findAllByIsSuperFalse();

    /**
     * Retrieves all permissions with the specified IDs.
     *
     * @param ids the set of permission IDs to retrieve
     * @return a set of {@link PermissionEntity} objects with the specified IDs
     */
    Set<PermissionEntity> findAllByIdIn(Set<String> ids);

}
