package org.ays.auth.service;

import org.ays.auth.model.User;
import org.ays.auth.model.request.UserSaveRequest;

/**
 * User Save Service to perform user related business operations.
 */
public interface UserSaveService {

    /**
     * Saves a saveRequest to the database.
     *
     * @param saveRequest the UserSaveRequest entity
     * @return User
     */
    User saveUser(UserSaveRequest saveRequest);

}
