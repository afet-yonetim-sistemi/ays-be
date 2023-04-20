package com.ays.user.service;

import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;

/**
 * User Auth service to perform user related authentication operations.
 */
public interface UserAuthService {

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
