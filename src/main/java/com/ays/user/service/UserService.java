package com.ays.user.service;

import com.ays.common.model.AysPage;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserSaveRequest;
import com.ays.user.model.dto.request.UserUpdateRequest;

/**
 * User service to perform user related business operations.
 */
public interface UserService {
    /**
     * Saves a saveRequest to the database.
     *
     * @param saveRequest the UserSaveRequest entity
     */
    void saveUser(UserSaveRequest saveRequest);

    /**
     * Get all Users from database.
     *
     * @param listRequest covering page and pageSize
     * @return User list
     */
    AysPage<User> getAllUsers(UserListRequest listRequest);

    /**
     * Get User by User ID
     *
     * @param id the given User ID
     * @return User
     */
    User getUserById(String id);

    /**
     * Delete Soft User by User ID
     *
     * @param id the given User ID
     */
    void deleteUser(String id);


    /**
     * Update User by User ID
     *
     * @param updateRequest the given UserUpdateRequest object
     */
    void updateUser(UserUpdateRequest updateRequest);
}
