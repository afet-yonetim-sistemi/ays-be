package org.ays.auth.repository;

import org.ays.auth.model.entity.AysPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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

}
