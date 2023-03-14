package com.ays.backend.user.service;

import com.ays.backend.user.controller.payload.request.RegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.service.dto.UserDTO;

/**
 * Auth service to perform user related authentication operations.
 */
public interface AuthService {

    /**
     * Register to platform.
     *
     * @param registerRequest the registerRequest entity
     * @return UserDTO
     */
    UserDTO register(RegisterRequest registerRequest);


    /**
     * Checks if the admin user by the given parameter exists in the database.
     *
     * @param username the given username
     * @return true if the admin user exists, false otherwise
     */
    Boolean existsByUsername(String username);
}
