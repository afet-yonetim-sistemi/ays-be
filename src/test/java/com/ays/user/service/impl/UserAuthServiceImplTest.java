package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UsernameNotValidException;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
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
    void givenAysLoginRequest_whenUserFound_thenReturnAuthenticatedUser() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        final AysToken mockToken = AysTokenBuilder.VALID_FOR_USER;

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(mockRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(tokenService.generate(userEntity.getClaims())).thenReturn(mockToken);

        // Then
        final AysToken token = userAuthService.authenticate(mockRequest);

        assertEquals(mockToken.getAccessToken(), token.getAccessToken());
        assertEquals(mockToken.getRefreshToken(), token.getRefreshToken());
        assertEquals(mockToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(mockRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(mockRequest.getPassword(), userEntity.getPassword());
        Mockito.verify(tokenService, Mockito.times(1)).generate(userEntity.getClaims());
    }

    @Test
    void givenInvalidAysLoginRequest_whenUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotValidException.class, () -> userAuthService.authenticate(mockRequest));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(mockRequest.getUsername());
    }

    @Test
    void givenInvalidAysLoginRequest_whenUserStatusNotActive_thenThrowUserNotActiveException() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntityBuilder()
                .withStatus(UserStatus.PASSIVE)
                .build();

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));

        // Then
        assertThrows(UserNotActiveException.class, () -> userAuthService.authenticate(mockRequest));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(mockRequest.getUsername());
    }

    @Test
    void givenInvalidAysLoginRequest_whenUserPasswordWrong_thenThrowPasswordNotValidException() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();
        final UserEntity userEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        PasswordNotValidException expectedError = new PasswordNotValidException();

        // When
        when(userRepository.findByUsername(mockRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(mockRequest.getUsername(), userEntity.getPassword())).thenReturn(false);

        // Then
        PasswordNotValidException actual =
                assertThrows(PasswordNotValidException.class, () -> userAuthService.authenticate(mockRequest));
        assertEquals(expectedError.getMessage(), actual.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(mockRequest.getUsername());
    }

    @Test
    void givenValidRefreshToken_whenAccessTokenGenerated_thenReturnAysToken() {
        // Given
        final String refreshToken = AysTokenBuilder.VALID_FOR_USER.getRefreshToken();

        final UserEntity userEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();
        final String username = userEntity.getUsername();

        final AysToken mockToken = AysTokenBuilder.VALID_FOR_USER;

        final Claims mockClaims = Jwts.claims();
        mockClaims.put(AysTokenClaims.USERNAME.getValue(), username);

        // When
        doNothing().when(tokenService).verifyAndValidate(refreshToken);
        when(tokenService.getClaims(refreshToken)).thenReturn(mockClaims);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(tokenService.generate(userEntity.getClaims(), refreshToken)).thenReturn(mockToken);

        // Then
        final AysToken token = userAuthService.refreshAccessToken(refreshToken);
        assertEquals(mockToken.getAccessToken(), token.getAccessToken());
        assertEquals(mockToken.getRefreshToken(), token.getRefreshToken());
        assertEquals(mockToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());

        Mockito.verify(tokenService, Mockito.times(1)).verifyAndValidate(refreshToken);
        Mockito.verify(tokenService, Mockito.times(1)).getClaims(refreshToken);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(tokenService, Mockito.times(1)).generate(userEntity.getClaims(), refreshToken);
    }

    @Test
    void givenRefreshTokenWithUsername_whenUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        final String refreshToken = AysTokenBuilder.VALID_FOR_USER.getRefreshToken();
        final UserEntity userEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();
        final String username = userEntity.getUsername();

        final Claims mockClaims = Jwts.claims();
        mockClaims.put(AysTokenClaims.USERNAME.getValue(), username);

        // When
        when(tokenService.getClaims(refreshToken)).thenReturn(mockClaims);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotValidException.class, () -> userAuthService.refreshAccessToken(refreshToken));

        Mockito.verify(tokenService, Mockito.times(1)).getClaims(refreshToken);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }
}