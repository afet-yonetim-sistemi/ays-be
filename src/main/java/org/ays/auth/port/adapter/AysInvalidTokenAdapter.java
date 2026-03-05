package org.ays.auth.port.adapter;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.common.client.AysCacheClient;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.port.AysParameterReadPort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * Adapter class implementing both {@link AysInvalidTokenReadPort} and {@link AysInvalidTokenSavePort} interfaces.
 * Handles invalid token persistence and lookup operations through the cache layer.
 */
@Component
@RequiredArgsConstructor
class AysInvalidTokenAdapter implements AysInvalidTokenReadPort, AysInvalidTokenSavePort {

    private final AysCacheClient cacheClient;
    private final AysParameterReadPort parameterReadPort;


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
     * The cache TTL is derived from the refresh token expiration parameter so that invalidation data
     * remains valid for the token lifecycle.
     * </p>
     *
     * @param accessTokenId  The access token ID to mark as invalid.
     * @param refreshTokenId The refresh token ID to mark as invalid.
     */
    @Override
    public void saveAll(final String accessTokenId,
                        final String refreshTokenId) {

        final AysParameter refreshTokenExpireMinuteParameter = parameterReadPort
                .findByName(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_MINUTE.name())
                .orElse(AysParameter.from(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_MINUTE));

        final long refreshTokenExpireMinutes = Long.parseLong(refreshTokenExpireMinuteParameter.getDefinition());
        final Duration timeToLive = Duration
                .ofMinutes(refreshTokenExpireMinutes);

        final Map<String, String> invalidTokens = Map.of(
                accessTokenId, "access",
                refreshTokenId, "refresh"
        );
        cacheClient.putAll(PREFIX, invalidTokens, timeToLive);
    }

}
