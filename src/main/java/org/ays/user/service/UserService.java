package org.ays.user.service;

import org.ays.common.model.AysPage;
import org.ays.user.model.User;
import org.ays.user.model.dto.request.UserListRequest;
import org.ays.user.model.dto.request.UserUpdateRequest;

/**
 * User service to perform user related business operations.
 */
public interface UserService {

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
     * Update User by User ID
     *
     * @param id            the given User ID
     * @param updateRequest the given UserUpdateRequest object
     */
    void updateUser(String id, UserUpdateRequest updateRequest);

    /**
     * Delete Soft User by User ID
     *
     * @param id the given User ID
     */
    void deleteUser(String id);

}
