package org.ays.auth.scheduler;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.service.AysParameterService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class InvalidTokenDeletionSchedulerTest extends AbstractUnitTest {

    @InjectMocks
    private InvalidTokenDeletionScheduler invalidTokenDeletionScheduler;

    @Mock
    private AysParameterService parameterService;

    @Mock
    private AysInvalidTokenRepository invalidTokenRepository;


    @Test
    void whenInvalidTokensToExpiredAfterExpirationOfRefreshToken_thenDeleteAllExpiredTokenIds() {

        // When
        AysParameter mockParameter = AysParameter
                .from(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY);
        Mockito.when(parameterService.findByName(Mockito.anyString()))
                .thenReturn(mockParameter);

        Mockito.doNothing()
                .when(invalidTokenRepository)
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));

        // Then
        invalidTokenDeletionScheduler.deleteInvalidTokens();

        // Verify
        Mockito.verify(parameterService, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(invalidTokenRepository, Mockito.times(1))
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));
    }

}
