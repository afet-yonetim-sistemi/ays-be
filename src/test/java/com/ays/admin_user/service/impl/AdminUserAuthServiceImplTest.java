package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.util.exception.AysAdminUserNotActiveException;
import com.ays.admin_user.util.exception.AysAdminUserNotVerifiedException;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysLoginRequestBuilder;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AdminUserAuthServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserAuthServiceImpl adminUserAuthService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private AysTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenValidLoginRequest_whenAdminUserAuthenticated_thenReturnAysToken() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(tokenService.generate(Mockito.anyMap()))
                .thenReturn(mockAysToken);

        // Then
        AysToken token = adminUserAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockAysToken, token);

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(1)).generate(Mockito.anyMap());
    }

    @Test
    void givenValidLoginRequest_whenAdminUserNotAuthenticated_thenReturnPasswordNotValidException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                PasswordNotValidException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void givenValidLoginRequest_whenAdminUserNotActive_thenReturnAysAdminUserNotActiveException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.PASSIVE).build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserNotActiveException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
    }

    @Test
    void givenValidLoginRequest_whenAdminUserNotVerified_thenReturnAysAdminUserNotVerifiedException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.NOT_VERIFIED).build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserNotVerifiedException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
    }

}
