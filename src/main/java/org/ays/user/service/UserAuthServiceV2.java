package org.ays.user.service;

import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.request.AysLoginRequestV2;

/**
 * User Auth service to perform user related authentication operations.
 */
public interface UserAuthServiceV2 {

    /**
     * Login to platform.
     *
     * @param loginRequest the AysLoginRequest entity
     * @return AysToken
     */
    AysToken authenticate(AysLoginRequestV2 loginRequest);

    /**
     * Refresh a Token
     *
     * @param refreshToken the refreshToken text
     * @return AysToken
     */
    AysToken refreshAccessToken(String refreshToken);


    /**
     * Invalidate Tokens
     *
     * @param refreshToken the refreshToken text
     */
    void invalidateTokens(String refreshToken);

}
