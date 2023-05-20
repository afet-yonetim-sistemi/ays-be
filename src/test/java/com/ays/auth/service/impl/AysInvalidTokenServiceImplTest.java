package com.ays.auth.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.entity.AysInvalidTokenEntity;
import com.ays.auth.model.entity.AysInvalidTokenEntityBuilder;
import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.auth.util.exception.TokenAlreadyInvalidatedException;
import com.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class AysInvalidTokenServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AysInvalidTokenServiceImpl invalidTokenService;

    @Mock
    private AysInvalidTokenRepository invalidTokenRepository;

    @Test
    void givenValidTokenIds_whenTokensInvalid_thenInvalidateTokens() {
        // Given
        Set<String> mockTokenIds = Set.of(AysRandomUtil.generateUUID(), AysRandomUtil.generateUUID());
        Set<AysInvalidTokenEntity> mockInvalidTokenEntities = mockTokenIds.stream()
                .map(tokenId -> AysInvalidTokenEntity.builder()
                        .tokenId(tokenId)
                        .build()
                )
                .collect(Collectors.toSet());

        // When
        Mockito.when(invalidTokenRepository.saveAll(mockInvalidTokenEntities))
                .thenReturn(mockInvalidTokenEntities.stream().toList());

        // Then
        invalidTokenService.invalidateTokens(mockTokenIds);

        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .saveAll(mockInvalidTokenEntities);
    }

    @Test
    void givenInvalidTokenId_whenTokenIdValidated_thenThrowTokenAlreadyInvalidatedException() {
        // Given
        AysInvalidTokenEntity mockInvalidTokenEntity = new AysInvalidTokenEntityBuilder().withValidValues().build();
        String mockTokenId = mockInvalidTokenEntity.getTokenId();

        // When
        Mockito.when(invalidTokenRepository.findByTokenId(mockTokenId))
                .thenReturn(Optional.of(mockInvalidTokenEntity));

        // Then
        Assertions.assertThrows(
                TokenAlreadyInvalidatedException.class,
                () -> invalidTokenService.checkForInvalidityOfToken(mockTokenId)
        );


        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .findByTokenId(mockTokenId);
    }

    @Test
    void givenValidTokenId_whenTokenIdValid_thenDoNothing() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(invalidTokenRepository.findByTokenId(mockTokenId))
                .thenReturn(Optional.empty());

        // Then
        invalidTokenService.checkForInvalidityOfToken(mockTokenId);

        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .findByTokenId(mockTokenId);
    }

}
