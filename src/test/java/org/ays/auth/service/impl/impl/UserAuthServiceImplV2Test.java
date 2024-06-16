package org.ays.auth.service.impl.impl;

import io.jsonwebtoken.Claims;
import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysTokenBuilder;
import org.ays.auth.model.entity.PermissionEntity;
import org.ays.auth.model.entity.PermissionEntityBuilder;
import org.ays.auth.model.entity.RoleEntity;
import org.ays.auth.model.entity.RoleEntityBuilder;
import org.ays.auth.model.entity.UserEntityV2;
import org.ays.auth.model.entity.UserEntityV2Builder;
import org.ays.auth.model.entity.UserLoginAttemptEntity;
import org.ays.auth.model.entity.UserLoginAttemptEntityBuilder;
import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.enums.SourcePage;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.request.AysLoginRequestV2;
import org.ays.auth.model.request.AysLoginRequestV2Builder;
import org.ays.auth.repository.UserLoginAttemptRepository;
import org.ays.auth.repository.UserRepositoryV2;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.auth.service.impl.UserAuthServiceImplV2;
import org.ays.auth.util.exception.EmailAddressNotValidException;
import org.ays.auth.util.exception.PasswordNotValidException;
import org.ays.auth.util.exception.TokenAlreadyInvalidatedException;
import org.ays.auth.util.exception.TokenNotValidException;
import org.ays.auth.util.exception.UserDoesNotAccessPageException;
import org.ays.auth.util.exception.UserIdNotValidException;
import org.ays.auth.util.exception.UserNotActiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

class UserAuthServiceImplV2Test extends AbstractUnitTest {

    @InjectMocks
    private UserAuthServiceImplV2 userAuthService;

    @Mock
    private UserRepositoryV2 userRepository;

    @Mock
    private UserLoginAttemptRepository userLoginAttemptRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AysTokenService tokenService;

    @Mock
    private AysInvalidTokenService invalidTokenService;


    @Mock
    private AysIdentity identity;

    @Test
    void givenValidLoginRequest_whenUserAuthenticated_thenReturnAysToken() {
        // Given
        AysLoginRequestV2 mockLoginRequest = new AysLoginRequestV2Builder()
                .withValidFields()
                .build();

        // When
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .build();
        Mockito.when(userRepository.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUserEntity.getPassword().getValue()))
                .thenReturn(true);

        UserLoginAttemptEntity mockUserLoginAttemptEntity = new UserLoginAttemptEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .build();
        Mockito.when(userLoginAttemptRepository.findByUserId(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserLoginAttemptEntity));

        mockUserLoginAttemptEntity.success();
        Mockito.when(userLoginAttemptRepository.save(Mockito.any(UserLoginAttemptEntity.class)))
                .thenReturn(mockUserLoginAttemptEntity);

        Mockito.when(tokenService.generate(Mockito.any(Claims.class)))
                .thenReturn(mockUserToken);

        // Then
        AysToken token = userAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockUserToken, token);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.times(1))
                .save(Mockito.any(UserLoginAttemptEntity.class));

        Mockito.verify(tokenService, Mockito.times(1))
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        AysLoginRequestV2 mockLoginRequest = new AysLoginRequestV2Builder()
                .withValidFields()
                .build();

        // When
        Mockito.when(userRepository.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                EmailAddressNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.never())
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .findByUserId(Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .save(Mockito.any(UserLoginAttemptEntity.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotAuthenticated_thenThrowPasswordNotValidException() {
        // Given
        AysLoginRequestV2 mockLoginRequest = new AysLoginRequestV2Builder()
                .withValidFields()
                .build();

        // When
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .build();
        Mockito.when(userRepository.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getEmailAddress(), mockUserEntity.getPassword().getValue()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                PasswordNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .findByUserId(Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .save(Mockito.any(UserLoginAttemptEntity.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenValidLoginRequest_whenUserNotActive_thenThrowUserNotActiveException() {
        // Given
        AysLoginRequestV2 mockLoginRequest = new AysLoginRequestV2Builder()
                .withValidFields()
                .build();

        // When
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withStatus(UserStatus.PASSIVE)
                .build();
        Mockito.when(userRepository.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .findByUserId(Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .save(Mockito.any(UserLoginAttemptEntity.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenValidLoginRequest_whenUserDoesNotAccessToPage_thenThrowUserDoesNotAccessException() {
        // Given
        AysLoginRequestV2 mockLoginRequest = new AysLoginRequestV2Builder()
                .withValidFields()
                .withSourcePage(SourcePage.INSTITUTION)
                .build();

        // When
        Set<PermissionEntity> mockPermissionEntities = Set.of(
                new PermissionEntityBuilder()
                        .withValidFields()
                        .withName("landing:page")
                        .withCategory(PermissionCategory.PAGE)
                        .build()
        );
        Set<RoleEntity> mockRoleEntities = Set.of(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withPermissions(mockPermissionEntities)
                        .build()
        );
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withRoles(mockRoleEntities)
                .build();
        Mockito.when(userRepository.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUserEntity.getPassword().getValue()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                UserDoesNotAccessPageException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .findByUserId(Mockito.anyString());

        Mockito.verify(userLoginAttemptRepository, Mockito.never())
                .save(Mockito.any(UserLoginAttemptEntity.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenValidated_thenReturnAysToken() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJBWVMiLCJpYXQiOjE3MTUzNTYzNzksImp0aSI6IjRiOTU5MzNm
                LWYxYmYtNGUyYy05MjBlLTdjMTMyYmNlNjg4NCIsImV4cCI6MTcxNTM2MzU3OSwiaW5zdGl0dXRpb25JZCI6ImM1ZjlmNjEwLWYxY
                zgtNDhhNC1iMWU5LTFmZWU5NWY1YTUxZiIsInVzZXJMYXN0TmFtZSI6IldpbGxpYW0iLCJ1c2VyRW1haWxBZGRyZXNzIjoiamFtZX
                Mud2lsbGlhbUBhZmV0eW9uZXRpbXNpc3RlbWkub3JnIiwidXNlckZpcnN0TmFtZSI6IkphbWVzIiwidXNlclBlcm1pc3Npb25zIjp
                bInVzZXI6bGlzdCIsInVzZXI6ZGVsZXRlIiwicm9sZTp1cGRhdGUiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246Y29uY2x1ZGUi
                LCJpbnN0aXR1dGlvbjpwYWdlIiwidXNlcjp1cGRhdGUiLCJyb2xlOmRldGFpbCIsImFwcGxpY2F0aW9uOnJlZ2lzdHJhdGlvbjpsa
                XN0Iiwic3VwZXIiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246ZGV0YWlsIiwidXNlcjpjcmVhdGUiLCJyb2xlOmNyZWF0ZSIsIn
                JvbGU6bGlzdCIsInJvbGU6ZGVsZXRlIiwidXNlcjpkZXRhaWwiXSwidXNlcklkIjoiZjg4MmNiOWMtOTM0MS00NzNiLWEwNDAtM2Z
                iZDA1YzA5YWM2In0.N5kX2l0H-L1R0MPM8y_dpSkuAmfpbWJJ34ZUVyeUbyiGmYN_2b9nshUfS93M7ouxVf5G4uTjZqjerWnq1lTV
                ikysiux582nKo9_HfMzm0N0QjgXqWCCx9eitRoga46t4hXQcQNKS8Hsuo_K-fi-eZErUamhnF1xbA9H3dG_tR8bv5QE8wB5jgHTTg
                X3BV2fylz8ut9z2beMF9-j8UlCASRBC9SIibb0n3zSGuEiD_NaCeWPPIVmNyB0ACfU78XGNc9tdlNQdFCaeQlldDyGSNUACEkI2F9
                q9hjtwiV-lNIrp1KxLwdpvQofxphnDJHjXCCgoLUG2ltS5IWlWAvd2yQ
                """;

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
        );
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));

        UserLoginAttemptEntity mockUserLoginAttemptEntity = new UserLoginAttemptEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .build();
        Mockito.when(userLoginAttemptRepository.findByUserId(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserLoginAttemptEntity));

        Claims mockClaimsOfUser = mockUserEntity.getClaims(mockUserLoginAttemptEntity);
        Mockito.when(tokenService.generate(mockClaimsOfUser, mockRefreshToken))
                .thenReturn(mockUserToken);

        // Then
        AysToken token = userAuthService.refreshAccessToken(mockRefreshToken);

        Assertions.assertEquals(mockUserToken.getAccessToken(), token.getAccessToken());
        Assertions.assertEquals(mockUserToken.getRefreshToken(), token.getRefreshToken());
        Assertions.assertEquals(mockUserToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .generate(Mockito.any(Claims.class), Mockito.anyString());
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
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);

        Mockito.verify(tokenService, Mockito.never())
                .getPayload(mockRefreshToken);

        Mockito.verify(invalidTokenService, Mockito.never())
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.never())
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class), Mockito.anyString());
    }

    @Test
    void givenInvalidRefreshToken_whenRefreshTokenAlreadyInvalidated_thenThrowTokenAlreadyInvalidatedException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJiNTFhODMwMC0xMjg4LTQxYzQtOGI4Zi03MGI3OTc3OTBlMDAi
                LCJpc3MiOiJBWVMiLCJpYXQiOjE2OTk4MTY3NTIsImV4cCI6MTY5OTkwMzE1MiwidXNlcklkIjoiYzRiNGU0ZGItNTY0MS00MWY3L
                TgyMjItYTc2ZGViMWMwNjVjIn0.aPPB30ihZqnZllGwjpzXbK_oH-BkY9T1xcuBqaKkmYMyoYPcMgiIwrIpdhduO3qmcSF7SuNydZ
                PDy6jdVkfzt_A_Y1xwihcLO_S3_gmtav5ydDBEmS5y1HizbnIWibEjiLe0j3gQF3cBySs5WPUWIaKFDx-3tqrd_wUan3-FbSSevO9
                zzd38NULAJNqwlHq_X1xz8j65vkJvN7jxQ9r1-ks_vzFg5MCrl60I4HzclznMfEiOOsCD_BCRWyBf985U5eELScOyRvx_SAaQ7xY4
                C5nJu1hFRj4AhPiLWOLWxbxmE2rrbMM8KdkiDhWiO9Y3sdDv3QRFEvRGRlk-HyWFrQ
                """;

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
        );
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doThrow(TokenAlreadyInvalidatedException.class)
                .when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        // Then
        Assertions.assertThrows(
                TokenAlreadyInvalidatedException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.never())
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class), Mockito.anyString());
    }

    @Test
    void givenValidRefreshToken_whenUsernameNotValid_thenThrowUsernameNotValidException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJBWVMiLCJpYXQiOjE3MTUzNTYzNzksImp0aSI6IjRiOTU5MzNm
                LWYxYmYtNGUyYy05MjBlLTdjMTMyYmNlNjg4NCIsImV4cCI6MTcxNTM2MzU3OSwiaW5zdGl0dXRpb25JZCI6ImM1ZjlmNjEwLWYxY
                zgtNDhhNC1iMWU5LTFmZWU5NWY1YTUxZiIsInVzZXJMYXN0TmFtZSI6IldpbGxpYW0iLCJ1c2VyRW1haWxBZGRyZXNzIjoiamFtZX
                Mud2lsbGlhbUBhZmV0eW9uZXRpbXNpc3RlbWkub3JnIiwidXNlckZpcnN0TmFtZSI6IkphbWVzIiwidXNlclBlcm1pc3Npb25zIjp
                bInVzZXI6bGlzdCIsInVzZXI6ZGVsZXRlIiwicm9sZTp1cGRhdGUiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246Y29uY2x1ZGUi
                LCJpbnN0aXR1dGlvbjpwYWdlIiwidXNlcjp1cGRhdGUiLCJyb2xlOmRldGFpbCIsImFwcGxpY2F0aW9uOnJlZ2lzdHJhdGlvbjpsa
                XN0Iiwic3VwZXIiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246ZGV0YWlsIiwidXNlcjpjcmVhdGUiLCJyb2xlOmNyZWF0ZSIsIn
                JvbGU6bGlzdCIsInJvbGU6ZGVsZXRlIiwidXNlcjpkZXRhaWwiXSwidXNlcklkIjoiZjg4MmNiOWMtOTM0MS00NzNiLWEwNDAtM2Z
                iZDA1YzA5YWM2In0.N5kX2l0H-L1R0MPM8y_dpSkuAmfpbWJJ34ZUVyeUbyiGmYN_2b9nshUfS93M7ouxVf5G4uTjZqjerWnq1lTV
                ikysiux582nKo9_HfMzm0N0QjgXqWCCx9eitRoga46t4hXQcQNKS8Hsuo_K-fi-eZErUamhnF1xbA9H3dG_tR8bv5QE8wB5jgHTTg
                X3BV2fylz8ut9z2beMF9-j8UlCASRBC9SIibb0n3zSGuEiD_NaCeWPPIVmNyB0ACfU78XGNc9tdlNQdFCaeQlldDyGSNUACEkI2F9
                q9hjtwiV-lNIrp1KxLwdpvQofxphnDJHjXCCgoLUG2ltS5IWlWAvd2yQ
                """;

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
        );
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UserIdNotValidException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class), Mockito.anyString());
    }

    @Test
    void givenValidRefreshToken_whenUserNotActive_thenThrowUserNotActiveException() {
        // Given
        String mockRefreshToken = """
                eyJ0eXAiOiJCZWFyZXIiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJBWVMiLCJpYXQiOjE3MTUzNTYzNzksImp0aSI6IjRiOTU5MzNm
                LWYxYmYtNGUyYy05MjBlLTdjMTMyYmNlNjg4NCIsImV4cCI6MTcxNTM2MzU3OSwiaW5zdGl0dXRpb25JZCI6ImM1ZjlmNjEwLWYxY
                zgtNDhhNC1iMWU5LTFmZWU5NWY1YTUxZiIsInVzZXJMYXN0TmFtZSI6IldpbGxpYW0iLCJ1c2VyRW1haWxBZGRyZXNzIjoiamFtZX
                Mud2lsbGlhbUBhZmV0eW9uZXRpbXNpc3RlbWkub3JnIiwidXNlckZpcnN0TmFtZSI6IkphbWVzIiwidXNlclBlcm1pc3Npb25zIjp
                bInVzZXI6bGlzdCIsInVzZXI6ZGVsZXRlIiwicm9sZTp1cGRhdGUiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246Y29uY2x1ZGUi
                LCJpbnN0aXR1dGlvbjpwYWdlIiwidXNlcjp1cGRhdGUiLCJyb2xlOmRldGFpbCIsImFwcGxpY2F0aW9uOnJlZ2lzdHJhdGlvbjpsa
                XN0Iiwic3VwZXIiLCJhcHBsaWNhdGlvbjpyZWdpc3RyYXRpb246ZGV0YWlsIiwidXNlcjpjcmVhdGUiLCJyb2xlOmNyZWF0ZSIsIn
                JvbGU6bGlzdCIsInJvbGU6ZGVsZXRlIiwidXNlcjpkZXRhaWwiXSwidXNlcklkIjoiZjg4MmNiOWMtOTM0MS00NzNiLWEwNDAtM2Z
                iZDA1YzA5YWM2In0.N5kX2l0H-L1R0MPM8y_dpSkuAmfpbWJJ34ZUVyeUbyiGmYN_2b9nshUfS93M7ouxVf5G4uTjZqjerWnq1lTV
                ikysiux582nKo9_HfMzm0N0QjgXqWCCx9eitRoga46t4hXQcQNKS8Hsuo_K-fi-eZErUamhnF1xbA9H3dG_tR8bv5QE8wB5jgHTTg
                X3BV2fylz8ut9z2beMF9-j8UlCASRBC9SIibb0n3zSGuEiD_NaCeWPPIVmNyB0ACfU78XGNc9tdlNQdFCaeQlldDyGSNUACEkI2F9
                q9hjtwiV-lNIrp1KxLwdpvQofxphnDJHjXCCgoLUG2ltS5IWlWAvd2yQ
                """;

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withStatus(UserStatus.PASSIVE)
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
        );
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class), Mockito.anyString());
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenAndAccessTokenValidated_thenInvalidateToken() {
        // Initialize
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        String mockAccessToken = mockUserToken.getAccessToken();
        Claims mockAccessTokenClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
        );
        String mockAccessTokenId = mockAccessTokenClaims.getId();

        // Given
        String mockRefreshToken = mockUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
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
        userAuthService.invalidateTokens(mockRefreshToken);

        // Verify
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
                () -> userAuthService.invalidateTokens(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.never())
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getAccessToken();

        Mockito.verify(invalidTokenService, Mockito.never())
                .invalidateTokens(Mockito.anySet());
    }

    @Test
    void givenInvalidatedRefreshToken_whenRefreshTokenInvalidated_thenThrowTokenAlreadyInvalidatedException() {
        // Initialize
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        // Given
        String mockRefreshToken = mockUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getEmailAddress()
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
                () -> userAuthService.invalidateTokens(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getAccessToken();

        Mockito.verify(invalidTokenService, Mockito.never())
                .invalidateTokens(Mockito.anySet());
    }

}
