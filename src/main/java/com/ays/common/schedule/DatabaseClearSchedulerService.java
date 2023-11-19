package com.ays.common.schedule;

import com.ays.auth.model.entity.AysInvalidTokenEntity;
import com.ays.auth.model.enums.AysConfigurationParameter;
import com.ays.auth.repository.AysInvalidTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseClearSchedulerService {

    private final AysInvalidTokenRepository aysInvalidTokenRepository;

    @Scheduled(cron = "0 0 */3 * *")
    public void clearInvalidTokens() {
        String refreshTokenExpireDayDefaultValue = AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue();
        LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(Long.parseLong(refreshTokenExpireDayDefaultValue));
        List<AysInvalidTokenEntity> allByCreatedAtBefore = aysInvalidTokenRepository.findAllByCreatedAtBefore(expirationThreshold);
        aysInvalidTokenRepository.deleteAll(allByCreatedAtBefore);

    }
}
