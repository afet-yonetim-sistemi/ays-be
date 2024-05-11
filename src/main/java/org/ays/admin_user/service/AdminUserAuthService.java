package org.ays.admin_user.service;

import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.request.AysLoginRequest;

/**
 * Admin Auth service to perform admin user related authentication operations.
 */
@Deprecated(since = "AdminUserAuthService V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public interface AdminUserAuthService {

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
