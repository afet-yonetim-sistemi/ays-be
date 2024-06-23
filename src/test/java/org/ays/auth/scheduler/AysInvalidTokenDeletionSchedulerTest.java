package org.ays.auth.scheduler;

import org.ays.AysUnitTest;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.port.AysInvalidTokenDeletePort;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.port.AysParameterReadPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

class AysInvalidTokenDeletionSchedulerTest extends AysUnitTest {

    @InjectMocks
    private AysInvalidTokenDeletionScheduler invalidTokenDeletionScheduler;

    @Mock
    private AysParameterReadPort parameterReadPort;

    @Mock
    private AysInvalidTokenDeletePort invalidTokenDeletePort;


    @Test
    void whenInvalidTokensToExpiredAfterExpirationOfRefreshToken_thenDeleteAllExpiredTokenIds() {

        // When
        Optional<AysParameter> mockParameter = Optional.of(
                AysParameter.from(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY)
        );
        Mockito.when(parameterReadPort.findByName(Mockito.anyString()))
                .thenReturn(mockParameter);

        Mockito.doNothing()
                .when(invalidTokenDeletePort)
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));

        // Then
        invalidTokenDeletionScheduler.deleteInvalidTokens();

        // Verify
        Mockito.verify(parameterReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(invalidTokenDeletePort, Mockito.times(1))
                .deleteAllByCreatedAtBefore(Mockito.any(LocalDateTime.class));
    }

}
