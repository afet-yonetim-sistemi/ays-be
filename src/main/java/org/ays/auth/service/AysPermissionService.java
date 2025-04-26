package org.ays.auth.service;

import org.ays.auth.model.AysPermission;

import java.util.List;

/**
 * Service interface for managing permissions.
 * Defines operations to retrieve permissions.
 */
public interface AysPermissionService {

    /**
     * Retrieves all available permissions from the system.
     * <p>
     * This method returns a complete list of {@link AysPermission} entities
     * without applying any filters such as role or permission type.
     * </p>
     *
     * @return a list of {@link AysPermission} objects representing all permissions
     */
    List<AysPermission> findAll();

}
