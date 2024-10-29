package org.ays.auth.port.adapter;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysInvalidToken;
import org.ays.auth.model.AysInvalidTokenBuilder;
import org.ays.auth.model.entity.AysInvalidTokenEntity;
import org.ays.auth.model.entity.AysInvalidTokenEntityBuilder;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class AysInvalidTokenAdapterTest extends AysUnitTest {


    @InjectMocks
    private AysInvalidTokenAdapter invalidTokenAdapter;

    @Mock
    private AysInvalidTokenRepository invalidTokenRepository;


    @Test
    void givenValidTokenId_whenInvalidTokenFound_thenReturnOptionalInvalidToken() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        AysInvalidTokenEntity mockInvalidTokenEntity = new AysInvalidTokenEntityBuilder()
                .withValidValues()
                .tokenId(mockTokenId)
                .build();

        Mockito
                .when(invalidTokenRepository.findByTokenId(Mockito.anyString()))
                .thenReturn(Optional.of(mockInvalidTokenEntity));

        // Then
        Optional<AysInvalidToken> invalidToken = invalidTokenAdapter.findByTokenId(mockTokenId);

        Assertions.assertTrue(invalidToken.isPresent());

        // Verify
        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .findByTokenId(Mockito.anyString());
    }


    @Test
    void givenValidTokenId_whenInvalidTokenNotFound_thenReturnOptionalEmpty() {
        // Given
        String mockTokenId = AysRandomUtil.generateUUID();

        // When
        Mockito
                .when(invalidTokenRepository.findByTokenId(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysInvalidToken> invalidToken = invalidTokenAdapter.findByTokenId(mockTokenId);

        Assertions.assertTrue(invalidToken.isEmpty());

        // Verify
        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .findByTokenId(Mockito.anyString());
    }


    @Test
    void givenValidInvalidTokens_whenTokensSaved_thenDoNothing() {
        // Given
        Set<AysInvalidToken> invalidTokens = Set.of(
                new AysInvalidTokenBuilder()
                        .withValidValues()
                        .build(),
                new AysInvalidTokenBuilder()
                        .withValidValues()
                        .build()
        );

        // When
        List<AysInvalidTokenEntity> mockInvalidTokenEntities = List.of(
                new AysInvalidTokenEntityBuilder().withValidValues().build()
        );

        Mockito
                .when(invalidTokenRepository.saveAll(Mockito.anyList()))
                .thenReturn(mockInvalidTokenEntities);

        // Then
        invalidTokenAdapter.saveAll(invalidTokens);

        // Verify
        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .saveAll(Mockito.anyList());
    }


    @Test
    void givenValidExpirationThreshold_whenTokensDeletedBeforeThresholdDate_thenDoNothing() {
        // Given
        LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(3);

        // When
        Mockito.doNothing()
                .when(invalidTokenRepository)
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));

        // Then
        invalidTokenAdapter.deleteAllByCreatedAtBefore(expirationThreshold);

        // Verify
        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));
    }

}
