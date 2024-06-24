package org.ays.auth.service;

import org.ays.auth.model.AysPermission;

import java.util.Set;

/**
 * Service interface for managing permissions.
 * Defines operations to retrieve permissions.
 */
public interface AysPermissionService {

    /**
     * Retrieves all permissions.
     *
     * @return A set of {@link AysPermission} objects representing all permissions in the system.
     */
    Set<AysPermission> findAll();

}
