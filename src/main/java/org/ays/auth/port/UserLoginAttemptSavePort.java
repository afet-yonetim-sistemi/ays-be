package org.ays.auth.port;

import org.ays.auth.model.AysUser;

/**
 * Port interface for saving user login attempt information.
 */
public interface UserLoginAttemptSavePort {

    /**
     * Saves the given user login attempt information.
     *
     * @param userId       the id of user
     * @param loginAttempt the login attempt information to save
     */
    void save(String userId, AysUser.LoginAttempt loginAttempt);

}
