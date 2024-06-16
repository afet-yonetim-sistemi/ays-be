package org.ays.user.service;

import org.ays.user.model.User;
import org.ays.user.model.request.UserSaveRequest;

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
