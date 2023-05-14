package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.TokenNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UsernameNotValidException;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    void givenValidLoginRequest_whenUserAuthenticated_thenReturnAysToken() {
        // Given
        AysLoginRequest mockLoginRequest = new AysUserLoginRequestBuilder().build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        // When
        Mockito.when(userRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getPassword(), mockUserEntity.getPassword()))
                .thenReturn(true);

        Mockito.when(tokenService.generate(mockUserEntity.getClaims()))
                .thenReturn(mockUserToken);

        // Then
        AysToken token = userAuthService.authenticate(mockLoginRequest);

        Assertions.assertEquals(mockUserToken, token);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(mockLoginRequest.getPassword(), mockUserEntity.getPassword());

        Mockito.verify(tokenService, Mockito.times(1))
                .generate(mockUserEntity.getClaims());
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotFound_thenThrowUsernameNotValidException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysUserLoginRequestBuilder().build();

        // When
        Mockito.when(userRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UsernameNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());
    }

    @Test
    void givenInvalidLoginRequest_whenUserNotAuthenticated_thenThrowPasswordNotValidException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysUserLoginRequestBuilder().build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        // When
        Mockito.when(userRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(passwordEncoder.matches(mockLoginRequest.getUsername(), mockUserEntity.getPassword()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                PasswordNotValidException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(mockLoginRequest.getPassword(), mockUserEntity.getPassword());
    }

    @Test
    void givenValidLoginRequest_whenUserNotActive_thenThrowUserNotActiveException() {
        // Given
        AysLoginRequest mockLoginRequest = new AysUserLoginRequestBuilder().build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.PASSIVE)
                .build();

        // When
        Mockito.when(userRepository.findByUsername(mockLoginRequest.getUsername()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> userAuthService.authenticate(mockLoginRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockLoginRequest.getUsername());
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

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockUserEntity.getUsername());

        AysToken mockAysToken = AysToken.builder()
                .accessToken("eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiI2NTYzZmMyMi03NGU1LTRmYmEtYTVmYy01MTBlMjg5NDIzOWUiLCJpc3MiOiJBWVMiLCJpYXQiOjE2ODIwMTk0NTEsImV4cCI6MTY4MjAyNjY1MSwidHlwZSI6IkJlYXJlciIsInVzZXJMYXN0TmFtZSI6IlNpc3RlbWkiLCJ1c2VyVHlwZSI6IkFETUlOIiwidXNlckZpcnN0TmFtZSI6IkFmZXQgWcO2bmV0aW0iLCJ1c2VybmFtZSI6ImF5cy1hZG1pbiJ9.FrJmW11zNJfzeYWhHINqcDTVNfwSCgKP9lmrRc9t4B3qfTSIJeMOQmEz4GfUTFwv26OGcgc7-OCEds9I5zCXP35OlSZ3rT4Wq-z0FgyPAGMmOgqg3J0s1gPn2joMt4ejhPdwKC0882Xq3K0d6baB4cRknB3vBtQcOgLwMeFn9jeWHisfO5cxe4SRc22cHSrrMPIW5KDpZXbUtpQag2BALcszpr62lCd5m1skrkBKr4siprys-XOId9zHTf8byn8mZJrDs3bX8dRNnPyNK8ol2rh1Ic81M_zS1sHd3hUMNTcW97kGZsj58Xc_DhBZZ0s_qXl0IvH4tD9Kv1ho7rYIDQ")
                .accessTokenExpiresAt(1682026651L)
                .refreshToken("eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiI3ZjljYTNmMy00N2FhLTRkNzQtOTAxYS0xNDdiYTJiNTQ3NGQiLCJpc3MiOiJBWVMiLCJpYXQiOjE2ODIwMTk0NTEsImV4cCI6MTY4MjEwNTg1MSwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.XBTCsqoQCs7BRiHkKU8VofJtnpel2iEWM5AJrZkh1SEnDZHt9T7PjLT5znSHCl6UKrpsVDt9yPqOjrvnLYuP4CGrf9l23nTkKQcCEs_M-U0Q1j7Y_eH0AwFvf9uEd5PBaC_Sv9cuwELhFSVTKzL7xh_cG1BBCQhB4NldkC7_OsNCHlYfiz31lo-0wf4F3wou02Gx1jQprMGaWGmOxAueFZE1_Mk-YcnL1SKBFkKSBZU_op4P_sH1FrLJ5VLsnfLhDRQSLteLvZ0mk-wvV6CZsRpfKKov77vjBIQr9aaE0aNLkh15JG8KWTpmBZDFt8e6dbA8TuUTPDWzGBIgrSRnQA")
                .build();

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(userRepository.findByUsername(mockUserEntity.getUsername()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(tokenService.generate(mockUserEntity.getClaims(), mockRefreshToken))
                .thenReturn(mockAysToken);

        // Then
        AysToken token = userAuthService.refreshAccessToken(mockRefreshToken);

        Assertions.assertEquals(mockAysToken.getAccessToken(), token.getAccessToken());
        Assertions.assertEquals(mockAysToken.getRefreshToken(), token.getRefreshToken());
        Assertions.assertEquals(mockAysToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockUserEntity.getUsername());
        Mockito.verify(tokenService, Mockito.times(1))
                .generate(mockUserEntity.getClaims(), mockRefreshToken);
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

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.ACTIVE)
                .build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockUserEntity.getUsername());

        // When
        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(userRepository.findByUsername(mockUserEntity.getUsername()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                UsernameNotValidException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockUserEntity.getUsername());
    }

    @Test
    void givenValidRefreshToken_whenUserNotActive_thenThrowUserNotActiveException() {
        // Given
        String mockRefreshToken = """
                eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJmOWE3OTFlZC04ZmJlLTRlMTAtYjMwZC0xYzQ4M2E4MTQyODgiLCJpc3MiOiJBWVMiLCJp
                YXQiOjE2ODMyMzA1NzQsImV4cCI6MTY4MzMxNjk3NCwidHlwZSI6IkJlYXJlciIsInVzZXJuYW1lIjoiYXlzLWFkbWluIn0.cj0Co
                k-JmiKwP3CVSSuEkMfnPfv3vSwRhmC0TKAqHmMPb2SlzJBwkKKWwsMH2Tqu3zCCrxUfO1qa4mqTDgNqHIsKYzUQLmMnKhAuKzBx0t
                CN7fkflGAAz1rqWl2oglQqnP3Xx183Zwm8qTo27M6cGFDZYmK9j106J4L9tSGbpQCuvg9pq4QXiyo7pHzWDgsGD2OuQpE4fqVcMq2
                ulZbJkqtYK_H0XSbJN8teYulSot4gAlYEryxGkUYSObvEmoMyAULeWBiPHl_riMbfDGIDZ-kEAZzre4a1K9nWDBvoPq_0VuFo4G2F
                B3pRmC-uK2dxsOMsp3dShHjT6aVUPw6-Zw
                """;

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withStatus(UserStatus.PASSIVE).build();

        Claims mockClaims = AysTokenBuilder.getValidClaims(mockUserEntity.getUsername());

        // When
        Mockito.doNothing().when(tokenService)
                .verifyAndValidate(mockRefreshToken);

        Mockito.when(tokenService.getClaims(mockRefreshToken))
                .thenReturn(mockClaims);

        Mockito.when(userRepository.findByUsername(mockUserEntity.getUsername()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                UserNotActiveException.class,
                () -> userAuthService.refreshAccessToken(mockRefreshToken)
        );

        Mockito.verify(tokenService, Mockito.times(1))
                .verifyAndValidate(mockRefreshToken);
        Mockito.verify(tokenService, Mockito.times(1))
                .getClaims(mockRefreshToken);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(mockUserEntity.getUsername());
    }

}
