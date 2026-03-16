package org.ays.auth.port.adapter;

import org.ays.AysUnitTest;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.common.client.AysCacheClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

class AysInvalidTokenAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysInvalidTokenAdapter invalidTokenAdapter;

    @Mock
    private AysCacheClient cacheClient;

    @Mock
    private AysApplicationConfigurationParameter applicationConfigurationParameter;


    private static final String MOCK_PREFIX = "ays_invalid_token";


    /**
     * {@link AysInvalidTokenAdapter#exists(String)}
     */
    @Test
    void givenValidTokenId_whenInvalidTokenFound_thenReturnOptionalInvalidToken() {

        // Given
        String mockTokenId = "bf252f65-c4e8-4ce2-878a-78da24322b3a";

        // When
        Mockito
                .when(cacheClient.find(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockTokenId)))
                .thenReturn(Optional.of("access"));

        // Then
        boolean exists = invalidTokenAdapter.exists(mockTokenId);

        Assertions.assertTrue(exists);

        // Verify
        Mockito.verify(cacheClient, Mockito.times(1))
                .find(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockTokenId));
    }

    @Test
    void givenValidTokenId_whenInvalidTokenNotFound_thenReturnOptionalEmpty() {

        // Given
        String mockTokenId = "07aaab21-4113-4a74-bdb7-2768687bbbae";

        // When
        Mockito
                .when(cacheClient.find(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockTokenId)))
                .thenReturn(Optional.empty());

        // Then
        boolean exists = invalidTokenAdapter.exists(mockTokenId);

        Assertions.assertFalse(exists);

        // Verify
        Mockito.verify(cacheClient, Mockito.times(1))
                .find(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockTokenId));
    }


    /**
     * {@link AysInvalidTokenAdapter#saveAll(String, String)}
     */
    @Test
    void givenValidInvalidTokens_whenTokensSaved_thenDoNothing() {

        // Given
        String mockAccessTokenId = "6d6e55d3-e0e8-4190-b24f-c25e8eb13201";
        String mockRefreshTokenId = "0cc37f36-667a-427f-ab0b-3642ede29e4c";

        // When
        int mockRefreshTokenExpireMinute = Integer.parseInt(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_MINUTE.getDefaultValue());
        Mockito
                .when(applicationConfigurationParameter.getRefreshTokenExpireMinute())
                .thenReturn(mockRefreshTokenExpireMinute);

        Map<String, String> mockInvalidTokens = Map.of(
                mockAccessTokenId, "access",
                mockRefreshTokenId, "refresh"
        );
        Duration mockTimeToLive = Duration.ofMinutes(mockRefreshTokenExpireMinute);
        Mockito.doNothing()
                .when(cacheClient)
                .putAll(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockInvalidTokens), Mockito.eq(mockTimeToLive));

        // Then
        invalidTokenAdapter.saveAll(mockAccessTokenId, mockRefreshTokenId);

        // Verify
        Mockito.verify(applicationConfigurationParameter, Mockito.times(1))
                .getRefreshTokenExpireMinute();

        Mockito.verify(cacheClient, Mockito.times(1))
                .putAll(Mockito.eq(MOCK_PREFIX), Mockito.eq(mockInvalidTokens), Mockito.eq(mockTimeToLive));
    }

}
