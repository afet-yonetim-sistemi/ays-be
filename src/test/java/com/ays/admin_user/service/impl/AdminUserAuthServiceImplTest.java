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

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(tokenService, Mockito.times(1)).generate(Mockito.anyMap());
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

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(Mockito.anyString(), Mockito.anyString());
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

        Mockito.verify(adminUserRepository, Mockito.times(1)).findByUsername(mockLoginRequest.getUsername());
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
                eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJmOWE3OTFlZC04ZmJlLTRlMTAtYjMwZC0xYzQ4M2E4MTQyODgiLCJpc3MiOiJBWVMiLCJp
                YXQiOjE2ODMyMzA1NzQsImV4cCI6MTY4MzMxNjk3NCwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.cj0Co
                k-JmiKwP3CVSSuEkMfnPfv3vSwRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAlYEryxGkUYSObvEmoMyAULeWBiPHl_riMbfDGIDZ-kEAZzre4a1K9nWDBvoPq_0VuFo4G2F
                B3pRmC-uK2dxsOMsp3dShHjT6aVUPw6-Zw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        AysToken mockAysToken = AysToken.builder()
                .accessToken("eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiI2NTYzZmMyMi03NGU1LTRmYmEtYTVmYy01MTBlMjg5NDIzOWUiLCJpc3MiOiJBWVMiLCJpYXQiOjE2ODIwMTk0NTEsImV4cCI6MTY4MjAyNjY1MSwidHlwZSI6IkJlYXJlciIsInVzZXJMYXN0TmFtZSI6IlNpc3RlbWkiLCJ1c2VyVHlwZSI6IkFETUlOIiwidXNlckZpcnN0TmFtZSI6IkFmZXQgWcO2bmV0aW0iLCJ1c2VybmFtZSI6ImF5cy1hZG1pbiJ9.FrJmW11zNJfzeYWhHINqcDTVNfwSCgKP9lmrRc9t4B3qfTSIJeMOQmEz4GfUTFwv26OGcgc7-OCEds9I5zCXP35OlSZ3rT4Wq-z0FgyPAGMmOgqg3J0s1gPn2joMt4ejhPdwKC0882Xq3K0d6baB4cRknB3vBtQcOgLwMeFn9jeWHisfO5cxe4SRc22cHSrrMPIW5KDpZXbUtpQag2BALcszpr62lCd5m1skrkBKr4siprys-XOId9zHTf8byn8mZJrDs3bX8dRNnPyNK8ol2rh1Ic81M_zS1sHd3hUMNTcW97kGZsj58Xc_DhBZZ0s_qXl0IvH4tD9Kv1ho7rYIDQ")
                .accessTokenExpiresAt(1682026651L)
                .refreshToken("eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiI3ZjljYTNmMy00N2FhLTRkNzQtOTAxYS0xNDdiYTJiNTQ3NGQiLCJpc3MiOiJBWVMiLCJpYXQiOjE2ODIwMTk0NTEsImV4cCI6MTY4MjEwNTg1MSwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.XBTCsqoQCs7BRiHkKU8VofJtnpel2iEWM5AJrZkh1SEnDZHt9T7PjLT5znSHCl6UKrpsVDt9yPqOjrvnLYuP4CGrf9l23nTkKQcCEs_M-U0Q1j7Y_eH0AwFvf9uEd5PBaC_Sv9cuwELhFSVTKzL7xh_cG1BBCQhB4NldkC7_OsNCHlYfiz31lo-0wf4F3wou02Gx1jQprMGaWGmOxAueFZE1_Mk-YcnL1SKBFkKSBZU_op4P_sH1FrLJ5VLsnfLhDRQSLteLvZ0mk-wvV6CZsRpfKKov77vjBIQr9aaE0aNLkh15JG8KWTpmBZDFt8e6dbA8TuUTPDWzGBIgrSRnQA")
                .build();

        // When
        Mockito.doNothing().when(tokenService).verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken)).thenReturn(mockClaims);

        Mockito.when(adminUserRepository.findByUsername(mockAdminUserEntity.getUsername()))
                .thenReturn(Optional.of(mockAdminUserEntity));

        Mockito.when(tokenService.generate(mockAdminUserEntity.getClaims(), mockRefreshToken))
                .thenReturn(mockAysToken);

        // Then
        AysToken token = adminUserAuthService.refreshAccessToken(mockRefreshToken);

        Assertions.assertEquals(mockAysToken.getAccessToken(), token.getAccessToken());
        Assertions.assertEquals(mockAysToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());
        Assertions.assertEquals(mockAysToken.getRefreshToken(), token.getRefreshToken());

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
                eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJmOWE3OTFlZC04ZmJlLTRlMTAtYjMwZC0xYzQ4M2E4MTQyODgiLCJpc3MiOiJBWVMiLCJp
                YXQiOjE2ODMyMzA1NzQsImV4cCI6MTY4MzMxNjk3NCwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.cj0Co
                k-JmiKwP3CVSSuEkMfnPfv3vSwRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAlYEryxGkUYSObvEmoMyAULeWBiPHl_riMbfDGIDZ-kEAZzre4a1K9nWDBvoPq_0VuFo4G2F
                B3pRmC-uK2dxsOMsp3dShHjT6aVUPw6-Zw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.ACTIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService).verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken)).thenReturn(mockClaims);

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
                eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJmOWE3OTFlZC04ZmJlLTRlMTAtYjMwZC0xYzQ4M2E4MTQyODgiLCJpc3MiOiJBWVMiLCJp
                YXQiOjE2ODMyMzA1NzQsImV4cCI6MTY4MzMxNjk3NCwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.cj0Co
                k-JmiKwP3CVSSuEkMfnPfv3vSwRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAlYEryxGkUYSObvEmoMyAULeWBiPHl_riMbfDGIDZ-kEAZzre4a1K9nWDBvoPq_0VuFo4G2F
                B3pRmC-uK2dxsOMsp3dShHjT6aVUPw6-Zw
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
                eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJmOWE3OTFlZC04ZmJlLTRlMTAtYjMwZC0xYzQ4M2E4MTQyODgiLCJpc3MiOiJBWVMiLCJp
                YXQiOjE2ODMyMzA1NzQsImV4cCI6MTY4MzMxNjk3NCwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.cj0Co
                k-JmiKwP3CVSSuEkMfnPfv3vSwRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAlYEryxGkUYSObvEmoMyAULeWBiPHl_riMbfDGIDZ-kEAZzre4a1K9nWDBvoPq_0VuFo4G2F
                B3pRmC-uK2dxsOMsp3dShHjT6aVUPw6-Zw
                """;

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withStatus(AdminUserStatus.NOT_VERIFIED).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockAdminUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService).verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken)).thenReturn(mockClaims);

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
