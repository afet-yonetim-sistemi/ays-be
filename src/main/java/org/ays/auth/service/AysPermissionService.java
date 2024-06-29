package org.ays.auth.service;

import org.ays.auth.model.AysPermission;

import java.util.List;

/**
 * Service interface for managing permissions.
 * Defines operations to retrieve permissions.
 */
public interface AysPermissionService {

    /**
     * Retrieves all permissions.
     *
     * @return A list of {@link AysPermission} objects representing all permissions in the system.
     */
    List<AysPermission> findAll();

}
