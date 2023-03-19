package com.ays.backend.user.service.impl;

import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.entities.RefreshToken;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.RefreshTokenRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${ays.token.refresh-token-expires-in}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByIdAndStatusNot(userId, UserStatus.PASSIVE).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found")));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);

    }

    @Override
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
        }

        return token;
    }

    @Override
    @Transactional
    public int deleteByUserIdForLogout(Long userId) {
        User user = userRepository.findByIdAndStatusNot(userId, UserStatus.PASSIVE).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return refreshTokenRepository.deleteByUser(user);
    }
}
