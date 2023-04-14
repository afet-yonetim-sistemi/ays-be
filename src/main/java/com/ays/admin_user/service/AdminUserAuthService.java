package com.ays.admin_user.service;

import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.model.AysToken;

/**
 * Auth service to perform user related authentication operations.
 */
public interface AdminUserAuthService {

    /**
     * Register to platform.
     *
     * @param registerRequest the registerRequest entity
     * @return UserDTO
     */
    void register(AdminUserRegisterRequest registerRequest);

    /**
     * Login to platform.
     *
     * @param loginRequest the loginRequest entity
     * @return Token
     */
    AysToken authenticate(AysLoginRequest loginRequest);

    /**
     * Refresh a Token
     *
     * @param refreshToken the refreshToken text
     * @return Token
     */
    AysToken refreshAccessToken(String refreshToken);

}
