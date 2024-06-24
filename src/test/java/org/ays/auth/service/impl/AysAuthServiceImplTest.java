package org.ays.auth.service.impl;

import io.jsonwebtoken.Claims;
import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysTokenBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysLoginRequestBuilder;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.auth.util.exception.AysEmailAddressNotValidException;
import org.ays.auth.util.exception.AysPasswordNotValidException;
import org.ays.auth.util.exception.AysTokenAlreadyInvalidatedException;
import org.ays.auth.util.exception.AysTokenNotValidException;
import org.ays.auth.util.exception.AysUserDoesNotAccessPageException;
import org.ays.auth.util.exception.AysUserIdNotValidException;
import org.ays.auth.util.exception.AysUserNotActiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

class AysAuthServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysAuthServiceImpl userAuthService;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AysTokenService tokenService;

    @Mock
    private AysInvalidTokenService invalidTokenService;


    @Mock
    private AysIdentity identity;

    @Test
    void givenValidLoginRequest_whenUserAuthenticatedForInstitution_thenReturnAysToken() {
        // Given
        AysSourcePage mockSourcePage = AysSourcePage.INSTITUTION;
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .withSourcePage(mockSourcePage)
                .build();

        // When
        Set<AysPermission> mockPermissions = Set.of(
                new AysPermissionBuilder()
                        .withValidValues()
                        .withName(mockSourcePage.getPermission())
                        .build()
        );
        Set<AysRole> mockRoles = Set.of(
                new AysRoleBuilder()
                        .withValidValues()
                        .withPermissions(mockPermissions)
                        .build()
        );
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withRoles(mockRoles)
                .withValidPassword()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUser.getPassword().getValue()))
                .thenReturn(true);

        Optional.ofNullable(mockUser.getLoginAttempt())
                .ifPresentOrElse(AysUser.LoginAttempt::success,
                        () -> {
                            mockUser.setLoginAttempt(AysUser.LoginAttempt.builder().build());
                            mockUser.getLoginAttempt().success();
                        });
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockUser);

        Mockito.when(tokenService.generate(Mockito.any(Claims.class)))
                .thenReturn(mockUserToken);

        // Then
        AysToken token = userAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockUserToken, token);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(tokenService, Mockito.times(1))
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenValidLoginRequest_whenUserAuthenticatedForLanding_thenReturnAysToken() {
        // Given
        AysSourcePage mockSourcePage = AysSourcePage.LANDING;
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .withSourcePage(mockSourcePage)
                .build();

        // When
        Set<AysPermission> mockPermissions = Set.of(
                new AysPermissionBuilder()
                        .withValidValues()
                        .withName(mockSourcePage.getPermission())
                        .build()
        );
        Set<AysRole> mockRoles = Set.of(
                new AysRoleBuilder()
                        .withValidValues()
                        .withPermissions(mockPermissions)
                        .build()
        );
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withRoles(mockRoles)
                .withValidPassword()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUser.getPassword().getValue()))
                .thenReturn(true);

        Optional.ofNullable(mockUser.getLoginAttempt())
                .ifPresentOrElse(AysUser.LoginAttempt::success,
                        () -> {
                            mockUser.setLoginAttempt(AysUser.LoginAttempt.builder().build());
                            mockUser.getLoginAttempt().success();
                        });
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockUser);

        Mockito.when(tokenService.generate(Mockito.any(Claims.class)))
                .thenReturn(mockUserToken);

        // Then
        AysToken token = userAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockUserToken, token);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(tokenService, Mockito.times(1))
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysEmailAddressNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.never())
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotAuthenticated_thenThrowPasswordNotValidException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withValidPassword()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getEmailAddress(), mockUser.getPassword().getValue()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysPasswordNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenValidLoginRequest_whenUserNotActive_thenThrowUserNotActiveException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withStatus(UserStatus.PASSIVE)
                .withValidPassword()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserNotActiveException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class));
    }

    @Test
    void givenValidLoginRequest_whenUserDoesNotAccessToPage_thenThrowUserDoesNotAccessException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withValidValues()
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // When
        Set<AysPermission> mockPermissions = Set.of(
                new AysPermissionBuilder()
                        .withValidValues()
                        .withName("landing:page")
                        .withCategory(PermissionCategory.PAGE)
                        .build()
        );
        Set<AysRole> mockRoles = Set.of(
                new AysRoleBuilder()
                        .withValidValues()
                        .withPermissions(mockPermissions)
                        .build()
        );
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockLoginRequest.getEmailAddress())
                .withRoles(mockRoles)
                .withValidPassword()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockLoginRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUser.getPassword().getValue()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserDoesNotAccessPageException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

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

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();

        Claims mockClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userReadPort.findById(mockUser.getId()))
                .thenReturn(Optional.of(mockUser));

        Claims mockClaimsOfUser = mockUser.getClaims();
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

        Mockito.verify(userReadPort, Mockito.times(1))
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
        Mockito.doThrow(AysTokenNotValidException.class)
                .when(tokenService).verifyAndValidate(mockRefreshToken);

        // Then
        Assertions.assertThrows(
                AysTokenNotValidException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);

        Mockito.verify(tokenService, Mockito.never())
                .getPayload(mockRefreshToken);

        Mockito.verify(invalidTokenService, Mockito.never())
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
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

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();

        Claims mockClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doThrow(AysTokenAlreadyInvalidatedException.class)
                .when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        // Then
        Assertions.assertThrows(
                AysTokenAlreadyInvalidatedException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
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

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();


        Claims mockClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userReadPort.findById(mockUser.getId()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserIdNotValidException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
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

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(UserStatus.PASSIVE)
                .build();

        Claims mockClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.doNothing().when(invalidTokenService)
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.when(userReadPort.findById(mockUser.getId()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserNotActiveException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        // Verify
        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.times(1))
                .getPayload(Mockito.anyString());

        Mockito.verify(invalidTokenService, Mockito.times(1))
                .checkForInvalidityOfToken(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(tokenService, Mockito.never())
                .generate(Mockito.any(Claims.class), Mockito.anyString());
    }


    @Test
    void givenValidRefreshToken_whenRefreshTokenAndAccessTokenValidated_thenInvalidateToken() {
        // Initialize
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();

        String mockAccessToken = mockUserToken.getAccessToken();
        Claims mockAccessTokenClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        String mockAccessTokenId = mockAccessTokenClaims.getId();

        // Given
        String mockRefreshToken = mockUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
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
        Mockito.doThrow(AysTokenNotValidException.class)
                .when(tokenService).verifyAndValidate(mockRefreshToken);

        // Then
        Assertions.assertThrows(
                AysTokenNotValidException.class,
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
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();

        // Given
        String mockRefreshToken = mockUserToken.getRefreshToken();
        Claims mockRefreshTokenClaims = AysTokenBuilder.addValidTokenClaims(mockUser.getClaims());
        String mockRefreshTokenId = mockRefreshTokenClaims.getId();

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getPayload(mockRefreshToken))
                .thenReturn(mockRefreshTokenClaims);

        Mockito.doThrow(AysTokenAlreadyInvalidatedException.class)
                .when(invalidTokenService).checkForInvalidityOfToken(mockRefreshTokenId);

        // Then
        Assertions.assertThrows(
                AysTokenAlreadyInvalidatedException.class,
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
