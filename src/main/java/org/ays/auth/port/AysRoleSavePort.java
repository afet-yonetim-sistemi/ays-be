package org.ays.auth.port;

import org.ays.auth.model.AysRole;

/**
 * Port interface for saving operations related to roles.
 */
public interface AysRoleSavePort {

    /**
     * Saves a role entity.
     *
     * @param role The role entity to be saved.
     * @return The saved role entity.
     */
    AysRole save(AysRole role);

}
