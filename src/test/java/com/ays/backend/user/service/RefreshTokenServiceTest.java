package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.entities.RefreshToken;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.entities.UserBuilder;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.RefreshTokenRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RefreshTokenServiceTest extends BaseServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    public void shouldFindByToken() {

        // given
        String token = "refresh-token";

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .build();

        // when
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundToken = refreshTokenService.findByToken(token);

        // then
        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get().getToken());
    }

    @Test
    public void shouldCreateRefreshTokenWhenUserNotFound() {

        // given
        Long userId = -1L;

        UserNotFoundException expectedError = new UserNotFoundException("User with id -1 not found");

        // when -  action or the behaviour that we are going test
        when(userRepository.findByIdAndStatusNot(eq(userId), eq(UserStatus.PASSIVE))).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class, () -> refreshTokenService.createRefreshToken(userId));

        // then
        assertEquals(expectedError.getMessage(), actual.getMessage());

    }

    @Test
    public void shouldCreateRefreshToken() {

        // given
        Long userId = 1L;
        Long refreshTokenDurationMs = 120000L;
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", refreshTokenDurationMs);

        User user = new UserBuilder()
                .withUserStatus(UserStatus.VERIFIED)
                .withUserRole(UserRole.ROLE_VOLUNTEER).build();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        // when
        when(userRepository.findByIdAndStatusNot(eq(userId), eq(UserStatus.PASSIVE))).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);


        RefreshToken createdToken = refreshTokenService.createRefreshToken(userId);

        // then
        assertNotNull(createdToken);
        assertEquals(user, createdToken.getUser());
        assertNotNull(createdToken.getToken());
        assertTrue(createdToken.getExpiryDate().isAfter(Instant.now()));
        assertTrue(createdToken.getExpiryDate().isBefore(Instant.now().plusMillis(refreshTokenDurationMs + 1000L)));
    }

    @Test
    public void shouldVerifyExpiration() {
        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setExpiryDate(Instant.now().minus(1, ChronoUnit.MINUTES));

        RefreshToken validToken = new RefreshToken();
        validToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));

        doNothing().when(refreshTokenRepository).delete(expiredToken);

        RefreshToken result1 = refreshTokenService.verifyExpiration(expiredToken);
        assertNull(result1.getToken());

        RefreshToken result2 = refreshTokenService.verifyExpiration(validToken);
        assertNotNull(result2);
        assertEquals(validToken, result2);
    }

    @Test
    public void shouldVerifyExpirationWhenExpired() {

        RefreshToken refreshToken = RefreshToken.builder()
                .expiryDate(Instant.now().minusMillis(1000L))
                .build();

        RefreshToken result = refreshTokenService.verifyExpiration(refreshToken);

        assertNull(result.getToken());
        assertNull(result.getUser());
    }


    @Test
    public void testDeleteByUserIdForLogout() {
        // given
        Long userId = 1L;

        User user = new UserBuilder()
                .withUserStatus(UserStatus.VERIFIED)
                .withUserRole(UserRole.ROLE_VOLUNTEER).build();

        // when
        when(userRepository.findByIdAndStatusNot(eq(userId), eq(UserStatus.PASSIVE))).thenReturn(Optional.of(user));
        when(refreshTokenRepository.deleteByUser(user)).thenReturn(1);

        int result = refreshTokenService.deleteByUserIdForLogout(userId);

        // then
        assertEquals(1, result);
    }

    @Test
    public void shouldDeleteByUserIdForLogoutWhenUserNotFound() {

        // given
        Long userId = -1L;

        UserNotFoundException expectedError = new UserNotFoundException("User with id -1 not found");

        // when -  action or the behaviour that we are going test
        when(userRepository.findByIdAndStatusNot(eq(userId), eq(UserStatus.PASSIVE))).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class, () -> refreshTokenService.createRefreshToken(userId));

        // then
        assertEquals(expectedError.getMessage(), actual.getMessage());

    }

}
