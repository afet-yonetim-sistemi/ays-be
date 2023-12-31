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


class UnusedInvalidTokenDeletionSchedulerTest extends AbstractSystemTest {
    @Value("${ays.scheduler.UnusedInvalidTokenDeletionScheduler.cron}")
    private String expectedCronExpression;

    @Autowired
    private ScheduledTaskHolder taskHolder;

    @SpyBean
    private UnusedInvalidTokenDeletionScheduler unusedInvalidTokenDeletionScheduler;

    private void initialize(AysInvalidTokenEntity mockAysInvalidTokenEntity) {
        invalidTokenRepository.save(mockAysInvalidTokenEntity);
    }

    @Test
    void whenUnusedInvalidTokenDeletionScheduled() {
        CronTask cronTask = taskHolder.getScheduledTasks()
                .stream()
                .map(ScheduledTask::getTask)
                .filter(CronTask.class::isInstance)
                .map(CronTask.class::cast)
                .filter(task -> task.toString().contains("common.scheduler.UnusedInvalidTokenDeletionScheduler"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No scheduled tasks"));

        Assertions.assertEquals(expectedCronExpression, cronTask.getExpression());
    }

    @Test
    void whenWait10Seconds_thenClearInvalidToken() {

        // When
        String refreshTokenExpireDay = AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue();
        AysInvalidTokenEntity mockAysInvalidTokenEntity = new AysInvalidTokenEntityBuilder()
                .withValidValues()
                .build();
        this.initialize(mockAysInvalidTokenEntity);
        mockAysInvalidTokenEntity.setCreatedAt(LocalDateTime.now().minusDays(Long.parseLong(refreshTokenExpireDay) + 2));
        this.initialize(mockAysInvalidTokenEntity);

        // Then
        Awaitility
                .await()
                .atMost(2, java.util.concurrent.TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Mockito.verify(unusedInvalidTokenDeletionScheduler, Mockito.atLeast(1)).scheduled();

                    AysInvalidTokenEntity entity = invalidTokenRepository.findById(mockAysInvalidTokenEntity.getId()).orElse(null);
                    Assertions.assertNull(entity);
                });

    }

}
