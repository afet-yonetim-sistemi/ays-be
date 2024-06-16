package org.ays.auth.service;

import org.ays.auth.model.Permission;

import java.util.List;

/**
 * Service interface for managing permissions.
 * <p>
 * The {@link PermissionService} interface defines the contract for permission-related operations.
 * It provides a method for retrieving all permissions available in the system.
 * </p>
 */
public interface PermissionService {

    /**
     * Retrieves all permissions available in the system.
     *
     * @return a list of {@link Permission} objects representing the permissions.
     */
    List<Permission> findAll();

}
