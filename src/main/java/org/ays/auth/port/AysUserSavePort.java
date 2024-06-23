package org.ays.auth.port;

import org.ays.auth.model.AysUser;

/**
 * Port interface for saving and updating user information.
 */
public interface AysUserSavePort {

    /**
     * Saves or updates the user information.
     *
     * @param user The user object to be saved or updated.
     * @return The saved or updated user object.
     */
    AysUser save(AysUser user);

}
