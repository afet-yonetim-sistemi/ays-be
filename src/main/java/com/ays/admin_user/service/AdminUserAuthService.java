package com.ays.admin_user.service;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.model.AysToken;

/**
 * Admin Auth service to perform admin user related authentication operations.
 */
public interface AdminUserAuthService {

    /**
     * Register to platform.
     *
     * @param registerRequest the AdminUserRegisterRequest entity
     */
    void register(AdminUserRegisterRequest registerRequest);

    /**
     * Login to platform.
     *
     * @param loginRequest the AysLoginRequest entity
     * @return AysToken
     */
    AysToken authenticate(AysLoginRequest loginRequest);

    /**
     * Refresh a Token
     *
     * @param refreshToken the refreshToken text
     * @return AysToken
     */
    AysToken refreshAccessToken(String refreshToken);

}
