package org.ays.auth.port;

import org.ays.auth.model.AysPermission;

import java.util.List;
import java.util.Set;

/**
 * Port interface for reading operations related to permissions.
 */
public interface AysPermissionReadPort {

    /**
     * Retrieves all permissions.
     *
     * @return A list containing all permissions.
     */
    List<AysPermission> findAll();

    /**
     * Retrieves all permissions where the 'isSuper' flag is false.
     *
     * @return A list containing permissions where 'isSuper' is false.
     */
    List<AysPermission> findAllByIsSuperFalse();

    /**
     * Retrieves all permissions by their IDs.
     *
     * @param permissionIds The set of permission IDs to retrieve.
     * @return A list containing permissions with the specified IDs.
     */
    List<AysPermission> findAllByIdIn(Set<String> permissionIds);

}
