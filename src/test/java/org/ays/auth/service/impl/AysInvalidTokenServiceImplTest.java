package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysInvalidToken;
import org.ays.auth.model.AysInvalidTokenBuilder;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.ays.auth.util.exception.AysTokenAlreadyInvalidatedException;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

class AysInvalidTokenServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysInvalidTokenServiceImpl invalidTokenService;

    @Mock
    private AysInvalidTokenReadPort invalidTokenReadPort;

    @Mock
    private AysInvalidTokenSavePort invalidTokenSavePort;


    @Test
    void givenValidTokenIds_whenTokensInvalid_thenInvalidateTokens() {
        // Given
        Set<String> mockTokenIds = Set.of(AysRandomUtil.generateUUID(), AysRandomUtil.generateUUID());

        // When
        Mockito.doNothing()
                .when(invalidTokenSavePort)
                .saveAll(Mockito.anySet());

        // Then
        invalidTokenService.invalidateTokens(mockTokenIds);

        // Verify
        Mockito.verify(invalidTokenSavePort, Mockito.times(1))
                .saveAll(Mockito.anySet());
    }

    @Test
    void givenInvalidTokenId_whenTokenIdValidated_thenThrowTokenAlreadyInvalidatedException() {
        // Given
        AysInvalidToken mockInvalidToken = new AysInvalidTokenBuilder().withValidValues().build();
        String mockTokenId = mockInvalidToken.getTokenId();

        // When
        Mockito.when(invalidTokenReadPort.findByTokenId(mockTokenId))
                .thenReturn(Optional.of(mockInvalidToken));

        // Then
        Assertions.assertThrows(
                AysTokenAlreadyInvalidatedException.class,
                () -> invalidTokenService.checkForInvalidityOfToken(mockTokenId)
        );

        // Verify
        Mockito.verify(invalidTokenReadPort, Mockito.times(1))
                .findByTokenId(mockTokenId);
    }

    @Test
    void givenValidTokenId_whenTokenIdValid_thenDoNothing() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(invalidTokenReadPort.findByTokenId(mockTokenId))
                .thenReturn(Optional.empty());

        // Then
        invalidTokenService.checkForInvalidityOfToken(mockTokenId);

        // Verify
        Mockito.verify(invalidTokenReadPort, Mockito.times(1))
                .findByTokenId(mockTokenId);
    }

}
