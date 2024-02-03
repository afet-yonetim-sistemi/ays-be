package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysLoginRequestBuilder;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.TokenAlreadyInvalidatedException;
import com.ays.auth.util.exception.TokenNotValidException;
import com.ays.auth.util.exception.UserIdNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UserNotVerifiedException;
import com.ays.auth.util.exception.UsernameNotValidException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

class AdminUserAuthServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserAuthServiceImpl adminUserAuthService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private AysTokenService tokenService;

    @Mock
    private AysInvalidTokenService invalidTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private AysIdentity identity;

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
        AysLoginRequest mockRequest = new AysLoginRequestBuilder().build();

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

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());
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

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                UserNotVerifiedException.class,
                () -> adminUserAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenValidated_thenReturnAysToken() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkNzVmOWFkMS04Njc4LTRhMWYtOWRlMi03MjY3NjgzYzQyZTUi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTQ4NTY3MzQsImV4cCI6MTY5NDk0MzEzNCwidXNlcklkIjoiOTI2MmYwZmMtOTNkYi00ZjdlL
                TgxYzYtYWFhZDg1YzJiMjA2In0.SVSZnIo2TnYz_tjlfpdzcokRM0waDC2ZdnlL5thjWHB3YSA5ZFwRg2vhqRj9-04UELO0W6XsBk
                B8dKf4HJBTJu1En2uRXqiB3zRuYHrdCL0tDBVzORUNAOdr9ZC_a3lDDFdE-sfPxvrxiPooIhJ3fphnPG5V0Bo0m3ngzFSnMMVyYDB
                F7x5Qq4f6WXsE7PWmoC40_2yI7X4k4lQV05luWyI27zjqcb3NNFDroWuk1Mwa06dqtcGMxL6o9AiksZk58Yvw3aR5e_FthiIXxVWh
                joK-v_J6szj0HNsUQ_9YKmXE2zOtUlirQ_lUOEVyz79NM4iIHv9-L8I6N_o9BIuOKw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(adminUserRepository.findById(mockAdminUserEntity.getId()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(tokenService.generate(mockAdminUserEntity.getClaims(), mockRefreshToken))
                .thenReturn(mockAdminUserToken);

        // Then
        AysToken token = adminUserAuthService.refreshAccessToken(mockRefreshToken);

        Assertions.assertEquals(mockAdminUserToken.getAccessToken(), token.getAccessToken());
        Assertions.assertEquals(mockAdminUserToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());
        Assertions.assertEquals(mockAdminUserToken.getRefreshToken(), token.getRefreshToken());

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findById(mockAdminUserEntity.getId());
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

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(0))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(0))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .findById(Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(0))
                .generate(Mockito.anyMap(), Mockito.anyString());

    }

    @Test
    void givenInvalidRefreshToken_whenRefreshTokenAlreadyInvalidated_thenThrowTokenAlreadyInvalidatedException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkNzVmOWFkMS04Njc4LTRhMWYtOWRlMi03MjY3NjgzYzQyZTUi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTQ4NTY3MzQsImV4cCI6MTY5NDk0MzEzNCwidXNlcklkIjoiOTI2MmYwZmMtOTNkYi00ZjdlL
                TgxYzYtYWFhZDg1YzJiMjA2In0.SVSZnIo2TnYz_tjlfpdzcokRM0waDC2ZdnlL5thjWHB3YSA5ZFwRg2vhqRj9-04UELO0W6XsBk
                B8dKf4HJBTJu1En2uRXqiB3zRuYHrdCL0tDBVzORUNAOdr9ZC_a3lDDFdE-sfPxvrxiPooIhJ3fphnPG5V0Bo0m3ngzFSnMMVyYDB
                F7x5Qq4f6WXsE7PWmoC40_2yI7X4k4lQV05luWyI27zjqcb3NNFDroWuk1Mwa06dqtcGMxL6o9AiksZk58Yvw3aR5e_FthiIXxVWh
                joK-v_J6szj0HNsUQ_9YKmXE2zOtUlirQ_lUOEVyz79NM4iIHv9-L8I6N_o9BIuOKw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doThrow(TokenAlreadyInvalidatedException.class)
                .when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        // Then
        Assertions.assertThrows(
                TokenAlreadyInvalidatedException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .findById(mockAdminUserEntity.getId());
        Mockito.verify(tokenService, Mockito.times(0))
                .generate(mockAdminUserEntity.getClaims(), mockRefreshToken);
    }

    @Test
    void givenValidRefreshToken_whenUsernameNotValid_thenThrowUsernameNotValidException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkNzVmOWFkMS04Njc4LTRhMWYtOWRlMi03MjY3NjgzYzQyZTUi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTQ4NTY3MzQsImV4cCI6MTY5NDk0MzEzNCwidXNlcklkIjoiOTI2MmYwZmMtOTNkYi00ZjdlL
                TgxYzYtYWFhZDg1YzJiMjA2In0.SVSZnIo2TnYz_tjlfpdzcokRM0waDC2ZdnlL5thjWHB3YSA5ZFwRg2vhqRj9-04UELO0W6XsBk
                B8dKf4HJBTJu1En2uRXqiB3zRuYHrdCL0tDBVzORUNAOdr9ZC_a3lDDFdE-sfPxvrxiPooIhJ3fphnPG5V0Bo0m3ngzFSnMMVyYDB
                F7x5Qq4f6WXsE7PWmoC40_2yI7X4k4lQV05luWyI27zjqcb3NNFDroWuk1Mwa06dqtcGMxL6o9AiksZk58Yvw3aR5e_FthiIXxVWh
                joK-v_J6szj0HNsUQ_9YKmXE2zOtUlirQ_lUOEVyz79NM4iIHv9-L8I6N_o9BIuOKw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(adminUserRepository.findById(mockAdminUserEntity.getId()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UserIdNotValidException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(0))
                .generate(Mockito.anyMap(), Mockito.anyString());
    }

    @Test
    void givenValidRefreshToken_whenAdminUserNotActive_thenThrowUserNotActiveException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkNzVmOWFkMS04Njc4LTRhMWYtOWRlMi03MjY3NjgzYzQyZTUi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTQ4NTY3MzQsImV4cCI6MTY5NDk0MzEzNCwidXNlcklkIjoiOTI2MmYwZmMtOTNkYi00ZjdlL
                TgxYzYtYWFhZDg1YzJiMjA2In0.SVSZnIo2TnYz_tjlfpdzcokRM0waDC2ZdnlL5thjWHB3YSA5ZFwRg2vhqRj9-04UELO0W6XsBk
                B8dKf4HJBTJu1En2uRXqiB3zRuYHrdCL0tDBVzORUNAOdr9ZC_a3lDDFdE-sfPxvrxiPooIhJ3fphnPG5V0Bo0m3ngzFSnMMVyYDB
                F7x5Qq4f6WXsE7PWmoC40_2yI7X4k4lQV05luWyI27zjqcb3NNFDroWuk1Mwa06dqtcGMxL6o9AiksZk58Yvw3aR5e_FthiIXxVWh
                joK-v_J6szj0HNsUQ_9YKmXE2zOtUlirQ_lUOEVyz79NM4iIHv9-L8I6N_o9BIuOKw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.PASSIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(adminUserRepository.findById(mockAdminUserEntity.getId()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(0))
                .generate(Mockito.anyMap(), Mockito.anyString());
    }

    @Test
    void givenValidRefreshToken_whenAdminUserNotVerified_thenThrowUserNotVerifiedException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkNzVmOWFkMS04Njc4LTRhMWYtOWRlMi03MjY3NjgzYzQyZTUi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTQ4NTY3MzQsImV4cCI6MTY5NDk0MzEzNCwidXNlcklkIjoiOTI2MmYwZmMtOTNkYi00ZjdlL
                TgxYzYtYWFhZDg1YzJiMjA2In0.SVSZnIo2TnYz_tjlfpdzcokRM0waDC2ZdnlL5thjWHB3YSA5ZFwRg2vhqRj9-04UELO0W6XsBk
                B8dKf4HJBTJu1En2uRXqiB3zRuYHrdCL0tDBVzORUNAOdr9ZC_a3lDDFdE-sfPxvrxiPooIhJ3fphnPG5V0Bo0m3ngzFSnMMVyYDB
                F7x5Qq4f6WXsE7PWmoC40_2yI7X4k4lQV05luWyI27zjqcb3NNFDroWuk1Mwa06dqtcGMxL6o9AiksZk58Yvw3aR5e_FthiIXxVWh
                joK-v_J6szj0HNsUQ_9YKmXE2zOtUlirQ_lUOEVyz79NM4iIHv9-L8I6N_o9BIuOKw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.NOT_VERIFIED).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(adminUserRepository.findById(mockAdminUserEntity.getId()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotVerifiedException.class,
                () -> adminUserAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(mockRefreshToken);
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(0))
                .generate(Mockito.anyMap(), Mockito.anyString());
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenAndAccessTokenValidated_thenInvalidateToken() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        String mockAccessToken = mockAdminUserToken.getAccessToken();
        Claims mockAccessTokenClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );
        String mockAccessTokenId = mockAccessTokenClaims.getId();

        String mockRefreshToken = mockAdminUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );
        String mockRefreshTokenId = mockRefreshTokenClaims.getId();

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockRefreshTokenClaims);
        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(mockRefreshTokenId);

        Mockito.when(identity.getAccessToken())
                .thenReturn(mockAccessToken);
        Mockito.when(tokenService.getPayload(mockAccessToken))
                .thenReturn(mockAccessTokenClaims);

        Mockito.doNothing().when(invalidTokenService)
                .invalidateTokens(Set.of(mockAccessTokenId, mockRefreshTokenId));

        // Then
        adminUserAuthService.invalidateTokens(mockRefreshToken);

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(2))
                .getPayload(Mockito.anyString());
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getAccessToken();

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .invalidateTokens(Mockito.anySet());
    }

    @Test
    void givenInvalidRefreshToken_whenRefreshTokenNotValid_thenThrowTokenNotValidException() {
        // Given
        String mockRefreshToken = "invalid_refresh_token";

        // When
        Mockito.doThrow(TokenNotValidException.class)
                .when(tokenService).verifyAndValidate(mockRefreshToken);

        // Then
        Assertions.assertThrows(
                TokenNotValidException.class,
                () -> adminUserAuthService.invalidateTokens(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(0))
                .getPayload(Mockito.anyString());
        Mockito.verify(invalidTokenService, Mockito.times(0))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(0))
                .getAccessToken();

        Mockito.verify(invalidTokenService, Mockito.times(0))
                .invalidateTokens(Mockito.anySet());
    }

    @Test
    void givenInvalidatedRefreshToken_whenRefreshTokenInvalidated_thenThrowTokenAlreadyInvalidatedException() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        String mockRefreshToken = mockAdminUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.getValidClaims(
                mockAdminUserEntity.getId(),
                mockAdminUserEntity.getUsername()
        );
        String mockRefreshTokenId = mockRefreshTokenClaims.getId();

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockRefreshTokenClaims);
        Mockito.doThrow(TokenAlreadyInvalidatedException.class)
                .when(invalidTokenService).checkForInvalidityOfToken(mockRefreshTokenId);

        // Then
        Assertions.assertThrows(
                TokenAlreadyInvalidatedException.class,
                () -> adminUserAuthService.invalidateTokens(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());
        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(0))
                .getAccessToken();

        Mockito.verify(invalidTokenService, Mockito.times(0))
                .invalidateTokens(Mockito.anySet());
    }

}
