package com.ays.backend.user.service;

import java.util.Optional;
import java.util.UUID;

import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.service.dto.UserDTO;

/**
 * User service to perform user related business operations.
 */
public interface UserService {
    /**
     * Saves a user to the database.
     *
     * @param user the user entity
     * @return userDto
     */
    UserDTO saveUser(User user);

    /**
     * Finds a user by given username
     *
     * @param username the username of the user.
     * @return the user, if present, empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if the user by the given parameter exists in the database.
     *
     * @param username the given username
     * @return true if the user exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Finds a user by the given uuid of a user.
     *
     * @param userUUID the given uuid of the user
     * @return the user, if present, empty if not found.
     */
    Optional<User> findByUserUUID(UUID userUUID);
}
