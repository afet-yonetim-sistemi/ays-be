package com.ays.backend.user.service;

import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRefreshTokenRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.model.Token;
import com.ays.backend.user.model.User;

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
    User register(AdminRegisterRequest registerRequest);

    /**
     * Login to platform.
     *
     * @param loginRequest the loginRequest entity
     * @return Token
     */
    Token login(AdminLoginRequest loginRequest);

    /**
     * Refresh a Token
     *
     * @param refreshTokenRequest the AdminRefreshTokenRequest entity
     * @return Token
     */
    Token refreshToken(AdminRefreshTokenRequest refreshTokenRequest);
}
