package com.ays.common.scheduler;

import com.ays.AbstractSystemTest;
import com.ays.auth.model.entity.AysInvalidTokenEntity;
import com.ays.auth.model.entity.AysInvalidTokenEntityBuilder;
import com.ays.auth.model.enums.AysConfigurationParameter;
import org.awaitility.Awaitility;
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
import java.util.List;
import java.util.Optional;

class InvalidTokenDeletionSchedulerSystemTest extends AbstractSystemTest {

    @Value("${ays.scheduler.invalid-tokens-deletion.cron}")
    private String expectedCronExpression;

    @Autowired
    private ScheduledTaskHolder taskHolder;

    @SpyBean
    private InvalidTokenDeletionScheduler invalidTokenDeletionScheduler;

    private void initialize(List<AysInvalidTokenEntity> mockInvalidTokenEntities) {
        invalidTokenRepository.saveAll(mockInvalidTokenEntities);
    }

    @Test
    void whenCronTaskUnusedInvalidTokenDeletionSchedulerCreated_thenCronTaskScheduled() {
        // Then
        CronTask cronTask = taskHolder.getScheduledTasks()
                .stream()
                .map(ScheduledTask::getTask)
                .filter(CronTask.class::isInstance)
                .map(CronTask.class::cast)
                .filter(task -> task.toString().contains("common.scheduler.InvalidTokenDeletionScheduler"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No scheduled tasks"));

        Assertions.assertEquals(expectedCronExpression, cronTask.getExpression());
    }

    @Test
    void whenWait10Seconds_thenClearInvalidToken() {

        // When
        int refreshTokenExpireDay = Integer
                .parseInt(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue());
        List<AysInvalidTokenEntity> mockInvalidTokenEntities = List.of(
                new AysInvalidTokenEntityBuilder()
                        .withValidValues()
                        .createdAt(LocalDateTime.now().minusDays(refreshTokenExpireDay + 1))
                        .build(),
                new AysInvalidTokenEntityBuilder()
                        .withValidValues()
                        .createdAt(LocalDateTime.now().minusDays(refreshTokenExpireDay + 1))
                        .build()
        );
        this.initialize(mockInvalidTokenEntities);

        // Then
        Awaitility
                .await()
                .atMost(2, java.util.concurrent.TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Mockito.verify(invalidTokenDeletionScheduler, Mockito.atLeast(1))
                            .deleteInvalidTokens();

                    mockInvalidTokenEntities.forEach(mockInvalidTokenEntity -> {
                        Optional<AysInvalidTokenEntity> invalidTokenEntity = invalidTokenRepository
                                .findById(mockInvalidTokenEntity.getId());
                        Assertions.assertTrue(invalidTokenEntity.isEmpty());
                    });
                });

    }

}
