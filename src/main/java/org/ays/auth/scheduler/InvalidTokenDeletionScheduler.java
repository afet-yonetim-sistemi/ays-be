package org.ays.auth.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.service.AysParameterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Scheduled task to delete invalid tokens that are no longer in use.
 * <p>
 * This scheduler is enabled based on the configuration property
 * {@code ays.scheduler.invalid-tokens-deletion.enable}.
 *
 * @see AysInvalidTokenRepository
 * @see AysParameterService
 * @see AysParameter
 * @see AysConfigurationParameter
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "ays.scheduler.invalid-tokens-deletion.enable", havingValue = "true")
class InvalidTokenDeletionScheduler {

    private final AysInvalidTokenRepository invalidTokenRepository;
    private final AysParameterService parameterService;

    public InvalidTokenDeletionScheduler(AysInvalidTokenRepository invalidTokenRepository,
                                         AysParameterService parameterService) {

        this.invalidTokenRepository = invalidTokenRepository;
        this.parameterService = parameterService;

        log.info("InvalidTokenDeletionScheduler is enabled.");
    }

    /**
     * Scheduled task method to delete invalid tokens that are no longer in use.
     * This method is triggered based on the cron expression specified in the configuration property
     * {@code ays.scheduler.invalid-tokens-deletion.cron}.
     * <p>
     * The method retrieves the expiration threshold for invalid tokens based on the configured refresh token
     * expiration day parameter. It then deletes all invalid tokens created before this threshold.
     *
     * @see AysInvalidTokenRepository#deleteAllByCreatedAtBefore(LocalDateTime)
     * @see AysParameterService#getParameter(String)
     * @see AysParameter
     * @see AysConfigurationParameter#AUTH_REFRESH_TOKEN_EXPIRE_DAY
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
