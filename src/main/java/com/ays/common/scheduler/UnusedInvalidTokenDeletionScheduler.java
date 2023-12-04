package com.ays.common.scheduler;

import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.service.AysParameterService;
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
    private final AysParameterService aysParameterService;

    /**
     * Clears all unused invalid tokens from the database.
     */
    @Scheduled(cron = "${ays.scheduler.UnusedInvalidTokenDeletionScheduler.cron}")
    @Transactional
    public void scheduled() {
        AysParameter refreshTokenExpireDayParameter = aysParameterService.getParameter("AUTH_REFRESH_TOKEN_EXPIRE_DAY");
        LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(Long.parseLong(refreshTokenExpireDayParameter.getDefinition()));
        aysInvalidTokenRepository.deleteAllByCreatedAtBefore(expirationThreshold);
    }

}
