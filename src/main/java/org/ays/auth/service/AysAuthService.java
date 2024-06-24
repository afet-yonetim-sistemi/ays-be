package org.ays.auth.service;

import org.ays.auth.model.AysToken;
import org.ays.auth.model.request.AysLoginRequest;

/**
 * Provides authentication and token management services for the AYS platform.
 * This interface defines methods for user authentication, refreshing access tokens,
 * and invalidating tokens, facilitating secure user sessions.
 * <p>
 * Implementations of this interface should handle the necessary logic to interact
 * with user credentials, validate tokens, and manage user sessions.
 */
public interface AysAuthService {

    /**
     * Authenticates a user based on the provided login request.
     * This method verifies the user's credentials and, if valid, generates an authentication token.
     *
     * @param loginRequest the {@link AysLoginRequest} containing the user's login credentials
     * @return an {@link AysToken} containing the access and refresh tokens for the authenticated session
     */
    AysToken authenticate(AysLoginRequest loginRequest);

    /**
     * Refreshes the access token using the provided refresh token.
     * This method validates the refresh token and issues a new access token if the refresh token is valid.
     *
     * @param refreshToken the refresh token used to obtain a new access token
     * @return a new {@link AysToken} containing the refreshed access and refresh tokens
     */
    AysToken refreshAccessToken(String refreshToken);

    /**
     * Invalidates the provided refresh token and associated access tokens.
     * This method ensures that the tokens can no longer be used for authentication or accessing resources.
     *
     * @param refreshToken the refresh token to be invalidated
     */
    void invalidateTokens(String refreshToken);

}
