package org.ays.auth.scheduler;

import org.awaitility.Awaitility;
import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysInvalidToken;
import org.ays.auth.model.AysInvalidTokenBuilder;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.port.AysInvalidTokenReadPort;
import org.ays.auth.port.AysInvalidTokenSavePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

class InvalidTokenDeletionSchedulerEndToEndTest extends AysEndToEndTest {

    @Value("${ays.scheduler.invalid-tokens-deletion.cron}")
    private String expectedCronExpression;

    @Autowired
    private ScheduledTaskHolder taskHolder;

    @SpyBean
    private AysInvalidTokenDeletionScheduler invalidTokenDeletionScheduler;


    @Autowired
    private AysInvalidTokenSavePort invalidTokenSavePort;

    @Autowired
    private AysInvalidTokenReadPort invalidTokenReadPort;


    @Test
    void whenCronTaskUnusedInvalidTokenDeletionSchedulerCreated_thenCronTaskScheduled() {
        // Then
        CronTask cronTask = taskHolder.getScheduledTasks()
                .stream()
                .map(ScheduledTask::getTask)
                .filter(CronTask.class::isInstance)
                .map(CronTask.class::cast)
                .filter(task -> task.toString().contains(AysInvalidTokenDeletionScheduler.class.getSimpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No scheduled tasks"));

        Assertions.assertEquals(expectedCronExpression, cronTask.getExpression());
    }

    @Test
    void whenWait2Seconds_thenClearInvalidToken() {

        // Initialize
        int refreshTokenExpireDay = Integer
                .parseInt(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue());
        Set<AysInvalidToken> mockInvalidTokens = Set.of(
                new AysInvalidTokenBuilder()
                        .withValidValues()
                        .withCreatedAt(LocalDateTime.now().minusDays(refreshTokenExpireDay + 1))
                        .build(),
                new AysInvalidTokenBuilder()
                        .withValidValues()
                        .withCreatedAt(LocalDateTime.now().minusDays(refreshTokenExpireDay + 1))
                        .build()
        );
        invalidTokenSavePort.saveAll(mockInvalidTokens);

        // Then
        Awaitility
                .await()
                .atMost(2, java.util.concurrent.TimeUnit.SECONDS)
                .untilAsserted(() -> {

                    Mockito.verify(invalidTokenDeletionScheduler, Mockito.atLeast(1))
                            .deleteInvalidTokens();

                    mockInvalidTokens.forEach(mockInvalidToken -> {

                        Optional<AysInvalidToken> invalidToken = invalidTokenReadPort
                                .findByTokenId(mockInvalidToken.getTokenId());

                        Assertions.assertTrue(invalidToken.isEmpty());
                    });
                });

    }

}
