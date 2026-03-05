package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysTokenAlreadyInvalidatedException;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class AysInvalidTokenServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysInvalidTokenServiceImpl invalidTokenService;

    @Mock
    private AysInvalidTokenReadPort invalidTokenReadPort;

    @Mock
    private AysInvalidTokenSavePort invalidTokenSavePort;


    /**
     * {@link AysInvalidTokenServiceImpl#invalidateTokens(String, String)}
     */
    @Test
    void givenValidTokenIds_whenTokensInvalid_thenInvalidateTokens() {

        // Given
        String mockAccessTokenId = "2420f22e-4e5f-4b18-8b73-3681e9dda072";
        String mockRefreshTokenId = "2a496b3b-d926-4494-910e-0a53fc894598";

        // Then
        invalidTokenService.invalidateTokens(mockAccessTokenId, mockRefreshTokenId);

        // Verify
        Mockito.verify(invalidTokenSavePort, Mockito.times(1))
                .saveAll(mockAccessTokenId, mockRefreshTokenId);
    }


    /**
     * {@link AysInvalidTokenServiceImpl#checkForInvalidityOfToken(String)}
     */
    @Test
    void givenInvalidTokenId_whenTokenIdValidated_thenThrowTokenAlreadyInvalidatedException() {

        // Given
        String mockTokenId = "dfa53c48-4030-4f88-a4f3-fa1212552b48";

        // When
        Mockito.when(invalidTokenReadPort.exists(mockTokenId))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysTokenAlreadyInvalidatedException.class,
                () -> invalidTokenService.checkForInvalidityOfToken(mockTokenId)
        );

        // Verify
        Mockito.verify(invalidTokenReadPort, Mockito.times(1))
                .exists(mockTokenId);
    }

    @Test
    void givenValidTokenId_whenTokenIdValid_thenDoNothing() {

        // Given
        String mockTokenId = "803cdf08-b7ca-4ab5-aa7d-e554f889194d";

        // When
        Mockito.when(invalidTokenReadPort.exists(mockTokenId))
                .thenReturn(false);

        // Then
        invalidTokenService.checkForInvalidityOfToken(mockTokenId);

        // Verify
        Mockito.verify(invalidTokenReadPort, Mockito.times(1))
                .exists(mockTokenId);
    }

}
