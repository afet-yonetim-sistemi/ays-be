package com.ays.backend.user.service;

import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User service to perform user related business operations.
 */
public interface UserService {
    /**
     * Saves a signUpRequest to the database.
     *
     * @param signUpRequest the signUpRequest entity
     * @return userDto
     */
    User saveUser(SignUpRequest signUpRequest);

    /**
     * Checks if the user by the given parameter exists in the database.
     *
     * @param pageable covering page and pageSize
     * @return userDto list
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * Get User DTO by User ID
     *
     * @param id the given User ID
     * @return userDto
     */
    User getUserById(Long id);

    /**
     * Delete Soft User by User ID
     *
     * @param id the given User ID
     * @return userDto
     */
    void deleteSoftUserById(Long id);


    /**
     * Update User by User ID
     *
     * @param updateUserRequest the given UpdateUserRequest object
     * @return userDto
     */
    User updateUserById(UpdateUserRequest updateUserRequest);
}
