package com.ays.common.scheduler;

import com.ays.auth.model.enums.AysConfigurationParameter;
import com.ays.auth.repository.AysInvalidTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Scheduler to remove all unused invalid token.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ays.scheduler.UnusedInvalidTokenDeletionScheduler.enabled", havingValue = "true", matchIfMissing = true)
class UnusedInvalidTokenDeletionScheduler {

    private final AysInvalidTokenRepository aysInvalidTokenRepository;

    /**
     * Clears all unused invalid tokens from the database.
     */
    @Scheduled(cron = "${ays.scheduler.UnusedInvalidTokenDeletionScheduler.cron}")
    @Transactional
    public void scheduled() {
        String refreshTokenExpireDay = AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue();
        LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(Long.parseLong(refreshTokenExpireDay));
        aysInvalidTokenRepository.deleteAllByCreatedAtBefore(expirationThreshold);
    }

}
