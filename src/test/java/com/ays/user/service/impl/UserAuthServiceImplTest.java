package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserNotActiveException;
import com.ays.user.util.exception.AysUserNotExistByUsernameException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserAuthServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AysTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldAuthenticateForUser() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(mockRequest.getPassword()));
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setRole(UserRole.VOLUNTEER);
        userEntity.setFirstName("First Name");
        userEntity.setLastName("Last Name");

        final AysToken token = AysTokenBuilder.VALID_FOR_USER;

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(mockRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(tokenService.generate(userEntity.getClaims())).thenReturn(token);

        // Then
        final AysToken result = userAuthService.authenticate(mockRequest);

        assertEquals(token.getAccessToken(), result.getAccessToken());
        assertEquals(token.getRefreshToken(), result.getRefreshToken());
        assertEquals(token.getAccessTokenExpiresAt(), result.getAccessTokenExpiresAt());
    }

    @Test
    void shouldAuthenticate_UserNotExist() {

        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        AysUserNotExistByUsernameException expectedError =
                new AysUserNotExistByUsernameException(mockRequest.getUsername());

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.empty());

        // Then
        AysUserNotExistByUsernameException actual =
                assertThrows(AysUserNotExistByUsernameException.class, () -> userAuthService.authenticate(mockRequest));
        assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Test
    void shouldAuthenticate_UserNotActive() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(mockRequest.getPassword()));
        userEntity.setStatus(UserStatus.PASSIVE);

        AysUserNotActiveException expectedError =
                new AysUserNotActiveException(mockRequest.getUsername());

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));

        // Then
        AysUserNotActiveException actual =
                assertThrows(AysUserNotActiveException.class, () -> userAuthService.authenticate(mockRequest));

        assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Test
    void shouldAuthenticate_PasswordNotValid() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode("wrongpassword"));
        userEntity.setStatus(UserStatus.ACTIVE);

        PasswordNotValidException expectedError = new PasswordNotValidException();

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(mockRequest.getUsername(), userEntity.getPassword())).thenReturn(false);

        // Then
        PasswordNotValidException actual =
                assertThrows(PasswordNotValidException.class, () -> userAuthService.authenticate(mockRequest));
        assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Test
    void shouldRefreshAccessTokenForUser() {
        // Given
        final String refreshToken = "test_refresh_token";
        final String username = "testuser";
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode("password"));
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setRole(UserRole.VOLUNTEER);
        userEntity.setFirstName("First Name");
        userEntity.setLastName("Last Name");

        final AysToken token = AysToken.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .accessTokenExpiresAt(10000L)
                .build();

        final Claims claims = Jwts.claims();
        claims.put(AysTokenClaims.USERNAME.getValue(), username);

        // When
        when(tokenService.getClaims(refreshToken)).thenReturn(claims);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(tokenService.generate(userEntity.getClaims(), refreshToken)).thenReturn(token);

        // Then
        final AysToken result = userAuthService.refreshAccessToken(refreshToken);
        assertEquals(token.getAccessToken(), result.getAccessToken());
        assertEquals(token.getRefreshToken(), result.getRefreshToken());
        assertEquals(token.getAccessTokenExpiresAt(), result.getAccessTokenExpiresAt());
    }

    @Test
    void shouldRefreshAccessToken_UserNotExist() {
        // Given
        final String refreshToken = "test_refresh_token";
        final String username = "testuser";

        AysUserNotExistByUsernameException expectedError =
                new AysUserNotExistByUsernameException(username);

        final Claims claims = Jwts.claims();
        claims.put(AysTokenClaims.USERNAME.getValue(), username);

        // When
        when(tokenService.getClaims(refreshToken)).thenReturn(claims);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        AysUserNotExistByUsernameException actual =
                assertThrows(AysUserNotExistByUsernameException.class, () -> userAuthService.refreshAccessToken(refreshToken));
        assertEquals(expectedError.getMessage(), actual.getMessage());
    }
}