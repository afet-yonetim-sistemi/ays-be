package com.ays.user.service;

import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSaveRequest;

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
