package org.ays.user.service;

import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.request.AysLoginRequest;

/**
 * User Auth service to perform user related authentication operations.
 */
@Deprecated(since = "UserAuthService V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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


    /**
     * Invalidate Tokens
     *
     * @param refreshToken the refreshToken text
     */
    void invalidateTokens(String refreshToken);

}
