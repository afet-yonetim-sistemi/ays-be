package org.ays.auth.port;

import org.ays.auth.model.AysPermission;

import java.util.Set;

/**
 * Port interface for reading operations related to permissions.
 */
public interface AysPermissionReadPort {

    /**
     * Retrieves all permissions.
     *
     * @return A set containing all permissions.
     */
    Set<AysPermission> findAll();

    /**
     * Retrieves all permissions where the 'isSuper' flag is false.
     *
     * @return A set containing permissions where 'isSuper' is false.
     */
    Set<AysPermission> findAllByIsSuperFalse();

    /**
     * Retrieves all permissions by their IDs.
     *
     * @param permissionIds The set of permission IDs to retrieve.
     * @return A set containing permissions with the specified IDs.
     */
    Set<AysPermission> findAllByIdIn(Set<String> permissionIds);

}
