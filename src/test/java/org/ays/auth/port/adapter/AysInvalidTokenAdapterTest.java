package org.ays.auth.port.adapter;

import org.ays.AysUnitTest;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.common.client.AysCacheClient;
import org.ays.common.util.AysRandomUtil;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.port.AysParameterReadPort;
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
    private AysParameterReadPort parameterReadPort;


    private static final String MOCK_PREFIX = "ays_invalid_token:";


    @Test
    void givenValidTokenId_whenInvalidTokenFound_thenReturnOptionalInvalidToken() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        Mockito
                .when(cacheClient.find(MOCK_PREFIX, mockTokenId))
                .thenReturn(Optional.of("access"));

        // Then
        boolean exists = invalidTokenAdapter.exists(mockTokenId);

        Assertions.assertTrue(exists);

        // Verify
        Mockito.verify(cacheClient, Mockito.times(1))
                .find(MOCK_PREFIX, mockTokenId);
    }


    @Test
    void givenValidTokenId_whenInvalidTokenNotFound_thenReturnOptionalEmpty() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        Mockito
                .when(cacheClient.find(MOCK_PREFIX, mockTokenId))
                .thenReturn(Optional.empty());

        // Then
        boolean exists = invalidTokenAdapter.exists(mockTokenId);

        Assertions.assertFalse(exists);

        // Verify
        Mockito.verify(cacheClient, Mockito.times(1))
                .find(MOCK_PREFIX, mockTokenId);
    }


    @Test
    void givenValidInvalidTokens_whenTokensSaved_thenDoNothing() {
        // Given
        String mockAccessTokenId = AysRandomUtil.generateUUID();
        String mockRefreshTokenId = AysRandomUtil.generateUUID();

        // When
        AysConfigurationParameter configurationParameter = AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_MINUTE;
        AysParameter mockParameter = AysParameter.from(configurationParameter);
        Mockito
                .when(parameterReadPort.findByName(configurationParameter.name()))
                .thenReturn(Optional.empty());

        Map<String, String> mockInvalidTokens = Map.of(
                mockAccessTokenId, "access",
                mockRefreshTokenId, "refresh"
        );
        Duration mockTimeToLive = Duration.ofMinutes(Long.parseLong(mockParameter.getDefinition()));
        Mockito.doNothing()
                .when(cacheClient)
                .putAll(MOCK_PREFIX, mockInvalidTokens, mockTimeToLive);

        // Then
        invalidTokenAdapter.saveAll(mockAccessTokenId, mockRefreshTokenId);

        // Verify
        Mockito.verify(parameterReadPort, Mockito.times(1))
                .findByName(configurationParameter.name());

        Mockito.verify(cacheClient, Mockito.times(1))
                .putAll(MOCK_PREFIX, mockInvalidTokens, mockTimeToLive);
    }

}
