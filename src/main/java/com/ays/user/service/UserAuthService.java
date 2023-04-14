package com.ays.user.service;

import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.model.AysToken;

/**
 * Auth service to perform user related authentication operations.
 */
public interface UserAuthService {

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
