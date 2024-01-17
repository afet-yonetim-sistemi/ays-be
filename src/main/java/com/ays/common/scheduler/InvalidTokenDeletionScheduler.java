package com.ays.common.scheduler;

import com.ays.auth.model.enums.AysConfigurationParameter;
import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.service.AysParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Scheduled task for deleting unused invalid tokens from the system.
 * This task is triggered based on a configured cron expression and a conditional property.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ays.scheduler.invalid-tokens-deletion.enable", havingValue = "true")
class InvalidTokenDeletionScheduler {

    private final AysInvalidTokenRepository invalidTokenRepository;
    private final AysParameterService parameterService;

    /**
     * Deletes invalid tokens created before a specified expiration threshold.
     * The scheduled task is executed at regular intervals based on the configured cron expression.
     */
    @Transactional
    @Scheduled(cron = "${ays.scheduler.invalid-tokens-deletion.cron}")
    public void deleteInvalidTokens() {
        final AysParameter refreshTokenExpireDayParameter = Optional
                .ofNullable(parameterService.getParameter(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.name()))
                .orElse(AysParameter.from(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY));

        LocalDateTime expirationThreshold = LocalDateTime.now()
                .minusDays(Long.parseLong(refreshTokenExpireDayParameter.getDefinition()));

        log.trace("Clearing all unused invalid tokens created before {}", expirationThreshold);
        invalidTokenRepository.deleteAllByCreatedAtBefore(expirationThreshold);
        log.trace("All unused invalid tokens created before {} have been cleared", expirationThreshold);
    }

}
