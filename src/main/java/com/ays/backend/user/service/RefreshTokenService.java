package com.ays.backend.user.service;

import com.ays.backend.user.model.entities.RefreshToken;

import java.util.Optional;

/**
 * Refresh Token Service to perform the process of refreshing access token.
 */
public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserIdForLogout(Long userId);
}
