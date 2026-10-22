package org.ays.auth.port.adapter;

import lombok.RequiredArgsConstructor;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.enums.AysTokenVariant;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.common.client.AysCacheClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Adapter class implementing both {@link AysInvalidTokenReadPort} and {@link AysInvalidTokenSavePort} interfaces.
 * Handles invalid token persistence and lookup operations through the cache layer.
 */
@Component
@RequiredArgsConstructor
class AysInvalidTokenAdapter implements AysInvalidTokenReadPort, AysInvalidTokenSavePort {

    private final AysCacheClient cacheClient;
    private final AysApplicationConfigurationParameter applicationConfigurationParameter;


    private static final String PREFIX = "ays_invalid_token";


    /**
     * Checks whether the given token ID exists in invalid token cache.
     *
     * @param tokenId The token ID to check for invalidation.
     * @return true if the token ID exists in cache, otherwise false.
     */
    @Override
    public boolean exists(final String tokenId) {
        return cacheClient.find(PREFIX, tokenId).isPresent();
    }


    /**
     * Stores both access and refresh token IDs as invalid entries in the cache.
     * <p>
     * Each entry's TTL is derived from its own token expiration parameter so that invalidation data
     * remains valid for the lifecycle of that token.
     * </p>
     *
     * @param accessTokenId  The access token ID to mark as invalid.
     * @param refreshTokenId The refresh token ID to mark as invalid.
     */
    @Override
    public void saveAll(final String accessTokenId,
                        final String refreshTokenId) {

        final Duration accessTokenTimeToLive = Duration
                .ofMinutes(applicationConfigurationParameter.getAccessTokenExpireMinute());
        cacheClient.put(PREFIX, accessTokenId, AysTokenVariant.ACCESS.name(), accessTokenTimeToLive);

        final Duration refreshTokenTimeToLive = Duration
                .ofMinutes(applicationConfigurationParameter.getRefreshTokenExpireMinute());
        cacheClient.put(PREFIX, refreshTokenId, AysTokenVariant.REFRESH.name(), refreshTokenTimeToLive);
    }

}
