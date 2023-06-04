package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysLoginRequestBuilder;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.*;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import io.jsonwebtoken.Claims;
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

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(tokenService.generate(Mockito.anyMap()))
                .thenReturn(mockAdminUserToken);

        // Then
        AysToken token = adminUserAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockAdminUserToken, token);

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(1))
                .generate(Mockito.anyMap());
    }

    @Test
    void givenInvalidLoginRequest_whenAdminUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockRequest.getUsername()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UsernameNotValidException.class,
                () -> adminUserAuthService.authenticate(mockRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockRequest.getUsername());
    }

    @Test
    void givenInvalidLoginRequest_whenAdminUserNotAuthenticated_thenThrowPasswordNotValidException() {
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

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void givenValidLoginRequest_whenAdminUserNotActive_thenThrowUserNotActiveException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.PASSIVE).build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());
    }

    @Test
    void givenValidLoginRequest_whenAdminUserNotVerified_thenThrowUserNotVerifiedException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.NOT_VERIFIED).build();

        // When
        Mockito.when(adminUserRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotVerifiedException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenValidated_thenReturnAysToken() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJhN2M1YWRmYi1kODQzLTQxMjAtYWY4NC1lZjFkZGE0YzU1NWMiL
                CJpc3MiOiJBWVMiLCJpYXQiOjE2ODU5MDMxMTQsImV4cCI6MTY4NTk4OTUxNCwidXNlcm5hbWUiOiJheXMtYWRtaW4ifQ.musUzHHs
                YBu7hf_H2d7sbyzeJa-2MPLqUZx6MPzVo4N1EE6fsFVkGVPlkaDCU2KtGKbZ7mIbpn3pDv-fGGOBh_WhFYerrv60HHn9ASSYICIT6d
                VgH2murJ_i3yQQ3gAFNOLeG9L_fl2vfhPdJFDJrrF7sY4S9z79R1jvz-LSbaK01s8IiQvnOOn9w4ASwFtlo9nnBfM7iJxqnxdFagPb
                SJaMR02FLDPQbEi7jW9fqYBZIKXaY5lGfwnQkonxn4uc6VwSP3G9DfOT1BTvu-Yie2XNv0KbT_nHs1szeZbmVuGF6sn8pJKYfQQiRf
                GiITu1qrV3LvdX0DTKt25gYYaHVQ
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(adminUserRepository.findByUsername(mockAdminUserEntity.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(tokenService.generate(mockAdminUserEntity.getClaims(), mockRefreshToken))
                .thenReturn(mockAdminUserToken);

        // Then
        AysToken token = adminUserAuthService.refreshAccessToken(mockRefreshToken);

        Assertions.assertEquals(mockAdminUserToken.getAccessToken(), token.getAccessToken());
        Assertions.assertEquals(mockAdminUserToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());
        Assertions.assertEquals(mockAdminUserToken.getRefreshToken(), token.getRefreshToken());

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockAdminUserEntity.getUsername());
        Mockito.verify(tokenService, Mockito.times(1))
                .generate(mockAdminUserEntity.getClaims(), mockRefreshToken);
    }

    @Test
    void givenInvalidRefreshToken_whenTokenNotVerifiedOrValidate_thenThrowTokenNotValidException() {
        // Given
        String mockRefreshToken = """
                eyJhbGciOiJSUzUxMiJ9.wRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAl
                """;

        // When
        Mockito.doThrow(TokenNotValidException.class)
                .when(tokenService).verifyAndValidate(mockRefreshToken);

        // Then
        Assertions.assertThrows(
                TokenNotValidException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
    }

    @Test
    void givenValidRefreshToken_whenUsernameNotValid_thenThrowUsernameNotValidException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJhN2M1YWRmYi1kODQzLTQxMjAtYWY4NC1lZjFkZGE0YzU1NWMiL
                CJpc3MiOiJBWVMiLCJpYXQiOjE2ODU5MDMxMTQsImV4cCI6MTY4NTk4OTUxNCwidXNlcm5hbWUiOiJheXMtYWRtaW4ifQ.musUzHHs
                YBu7hf_H2d7sbyzeJa-2MPLqUZx6MPzVo4N1EE6fsFVkGVPlkaDCU2KtGKbZ7mIbpn3pDv-fGGOBh_WhFYerrv60HHn9ASSYICIT6d
                VgH2murJ_i3yQQ3gAFNOLeG9L_fl2vfhPdJFDJrrF7sY4S9z79R1jvz-LSbaK01s8IiQvnOOn9w4ASwFtlo9nnBfM7iJxqnxdFagPb
                SJaMR02FLDPQbEi7jW9fqYBZIKXaY5lGfwnQkonxn4uc6VwSP3G9DfOT1BTvu-Yie2XNv0KbT_nHs1szeZbmVuGF6sn8pJKYfQQiRf
                GiITu1qrV3LvdX0DTKt25gYYaHVQ
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(adminUserRepository.findByUsername(mockAdminUserEntity.getUsername()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UsernameNotValidException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockAdminUserEntity.getUsername());
    }

    @Test
    void givenValidRefreshToken_whenAdminUserNotActive_thenThrowUserNotActiveException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJhN2M1YWRmYi1kODQzLTQxMjAtYWY4NC1lZjFkZGE0YzU1NWMiL
                CJpc3MiOiJBWVMiLCJpYXQiOjE2ODU5MDMxMTQsImV4cCI6MTY4NTk4OTUxNCwidXNlcm5hbWUiOiJheXMtYWRtaW4ifQ.musUzHHs
                YBu7hf_H2d7sbyzeJa-2MPLqUZx6MPzVo4N1EE6fsFVkGVPlkaDCU2KtGKbZ7mIbpn3pDv-fGGOBh_WhFYerrv60HHn9ASSYICIT6d
                VgH2murJ_i3yQQ3gAFNOLeG9L_fl2vfhPdJFDJrrF7sY4S9z79R1jvz-LSbaK01s8IiQvnOOn9w4ASwFtlo9nnBfM7iJxqnxdFagPb
                SJaMR02FLDPQbEi7jW9fqYBZIKXaY5lGfwnQkonxn4uc6VwSP3G9DfOT1BTvu-Yie2XNv0KbT_nHs1szeZbmVuGF6sn8pJKYfQQiRf
                GiITu1qrV3LvdX0DTKt25gYYaHVQ
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.PASSIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService).verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken)).thenReturn(mockClaims);

        Mockito.when(adminUserRepository.findByUsername(mockAdminUserEntity.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockAdminUserEntity.getUsername());
    }

    @Test
    void givenValidRefreshToken_whenAdminUserNotVerified_thenThrowUserNotVerifiedException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJhN2M1YWRmYi1kODQzLTQxMjAtYWY4NC1lZjFkZGE0YzU1NWMiL
                CJpc3MiOiJBWVMiLCJpYXQiOjE2ODU5MDMxMTQsImV4cCI6MTY4NTk4OTUxNCwidXNlcm5hbWUiOiJheXMtYWRtaW4ifQ.musUzHHs
                YBu7hf_H2d7sbyzeJa-2MPLqUZx6MPzVo4N1EE6fsFVkGVPlkaDCU2KtGKbZ7mIbpn3pDv-fGGOBh_WhFYerrv60HHn9ASSYICIT6d
                VgH2murJ_i3yQQ3gAFNOLeG9L_fl2vfhPdJFDJrrF7sY4S9z79R1jvz-LSbaK01s8IiQvnOOn9w4ASwFtlo9nnBfM7iJxqnxdFagPb
                SJaMR02FLDPQbEi7jW9fqYBZIKXaY5lGfwnQkonxn4uc6VwSP3G9DfOT1BTvu-Yie2XNv0KbT_nHs1szeZbmVuGF6sn8pJKYfQQiRf
                GiITu1qrV3LvdX0DTKt25gYYaHVQ
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.NOT_VERIFIED).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(adminUserRepository.findByUsername(mockAdminUserEntity.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotVerifiedException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockAdminUserEntity.getUsername());
    }

}
