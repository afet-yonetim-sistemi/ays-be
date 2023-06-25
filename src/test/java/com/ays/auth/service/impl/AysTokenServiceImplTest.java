package com.ays.auth.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.auth.config.AysTokenConfigurationParameter;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.auth.util.KeyConverter;
import com.ays.auth.util.exception.TokenNotValidException;
import com.ays.common.util.AysListUtil;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class AysTokenServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AysTokenServiceImpl tokenService;

    @Mock
    private AysTokenConfigurationParameter tokenConfiguration;

    @Test
    void givenValidAdminUserClaims_whenTokensGenerated_thenReturnAysToken() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().build();
        Map<String, Object> mockAdminUserClaims = mockAdminUserEntity.getClaims();

        // When
        Mockito.when(tokenConfiguration.getIssuer()).thenReturn(MOCK_ISSUER);
        Mockito.when(tokenConfiguration.getAccessTokenExpireMinute()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRE_MINUTE);
        Mockito.when(tokenConfiguration.getRefreshTokenExpireDay()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRE_DAY);
        Mockito.when(tokenConfiguration.getPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);

        // Then
        AysToken aysToken = tokenService.generate(mockAdminUserClaims);

        Assertions.assertNotNull(aysToken);
        Assertions.assertNotNull(aysToken.getAccessToken());
        Assertions.assertNotNull(aysToken.getAccessTokenExpiresAt());
        Assertions.assertNotNull(aysToken.getRefreshToken());

        Mockito.verify(tokenConfiguration, Mockito.times(2)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(2)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidUserClaims_whenTokensGenerated_thenReturnAysToken() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();
        Map<String, Object> mockUserClaims = mockUserEntity.getClaims();

        // When
        Mockito.when(tokenConfiguration.getIssuer()).thenReturn(MOCK_ISSUER);
        Mockito.when(tokenConfiguration.getAccessTokenExpireMinute()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRE_MINUTE);
        Mockito.when(tokenConfiguration.getRefreshTokenExpireDay()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRE_DAY);
        Mockito.when(tokenConfiguration.getPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);

        // Then
        AysToken aysToken = tokenService.generate(mockUserClaims);

        Assertions.assertNotNull(aysToken);
        Assertions.assertNotNull(aysToken.getAccessToken());
        Assertions.assertNotNull(aysToken.getAccessTokenExpiresAt());
        Assertions.assertNotNull(aysToken.getRefreshToken());

        Mockito.verify(tokenConfiguration, Mockito.times(2)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(2)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidAdminUserClaimsAndRefreshToken_whenAccessTokenGenerated_thenReturnAysToken() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().build();
        Map<String, Object> mockAdminUserClaims = mockAdminUserEntity.getClaims();

        // When
        Mockito.when(tokenConfiguration.getIssuer()).thenReturn(MOCK_ISSUER);
        Mockito.when(tokenConfiguration.getAccessTokenExpireMinute()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRE_MINUTE);
        Mockito.when(tokenConfiguration.getRefreshTokenExpireDay()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRE_DAY);
        Mockito.when(tokenConfiguration.getPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);

        // Then
        AysToken aysToken = tokenService.generate(mockAdminUserClaims, mockAdminUserToken.getRefreshToken());

        Assertions.assertNotNull(aysToken);
        Assertions.assertNotNull(aysToken.getAccessToken());
        Assertions.assertNotNull(aysToken.getAccessTokenExpiresAt());
        Assertions.assertNotNull(aysToken.getRefreshToken());

        Mockito.verify(tokenConfiguration, Mockito.times(1)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidUserClaimsAndRefreshToken_whenAccessTokenGenerated_thenReturnAysToken() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();
        Map<String, Object> mockUserClaims = mockUserEntity.getClaims();

        // When
        Mockito.when(tokenConfiguration.getIssuer()).thenReturn(MOCK_ISSUER);
        Mockito.when(tokenConfiguration.getAccessTokenExpireMinute()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRE_MINUTE);
        Mockito.when(tokenConfiguration.getRefreshTokenExpireDay()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRE_DAY);
        Mockito.when(tokenConfiguration.getPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);

        // Then
        AysToken aysToken = tokenService.generate(mockUserClaims, mockUserToken.getRefreshToken());

        Assertions.assertNotNull(aysToken);
        Assertions.assertNotNull(aysToken.getAccessToken());
        Assertions.assertNotNull(aysToken.getAccessTokenExpiresAt());
        Assertions.assertNotNull(aysToken.getRefreshToken());

        Mockito.verify(tokenConfiguration, Mockito.times(1)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidJwt_whenJwtVerifiedAndValidate_thenDoNothing() {
        // Given
        long currentTimeMillis = System.currentTimeMillis();
        String mockJwt = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(MOCK_ISSUER)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY, SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .compact();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        tokenService.verifyAndValidate(mockJwt);

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenInvalidJwt_whenJwtNotParsed_thenThrowTokenNotValidException() {
        // Given
        String mockJwt = "eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiJkYjhlNmZiYy1hMDJmLTQwMDQtOTU4ZC02M2U5MmM4ZTllM2QiLCJpc3MiOiJBWVMiLCJpYXQiOjE2ODIwMTk1MTksImV4cCI6MTY4MjAyNjcxOSwidHlwZSI6IkJlYXJlciIsInVzZXJMYXN0TmFtZSI6IlNpc3RlbWkiLCJyb2xlcyI6WyJWT0xVTlRFRVIiXSwidXNlclR5cGUiOiJVU0VSIiwidXNlckZpcnN0TmFtZSI6IkFmZXQgWcO2bmV0aW0iLCJ1c2VybmFtZSI6IjIzMjE4MCJ9.JsbCDRMy2nqMb5tjcY5IZSf0jtEp4PdJQMMZdn8lYvrSkspHGxcTNMyr8P6r8JC3t1qInAuGbybuKG6COr9LTzPB9TO15x_58zxFAqRrq9VYDb-nQhjPs9auNjfL1W1HjUd29zqA4E9ZsviVA3bbHv29Uu-PYuUMXU6Oqh4ahnrngeip4mufSfEr46voLi_QUoAmzX34cgLn526FaBA3QBqtc8GJVL5fCCTC1WXy-nTSgyUMow8gRSQowYMtm9qqlMxml-NEOKU2xyJXzpyiaf7GDErvgKwMW6CxkEFI91rt_KcM88xc3uXcz8yy6m8ZZZzff54VZ9Cp9FujD6ubtg";

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        Assertions.assertThrows(
                TokenNotValidException.class,
                () -> tokenService.verifyAndValidate(mockJwt)
        );

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidJwt_whenJwtParsed_thenReturnAdminUserClaims() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().build();
        Map<String, Object> mockAdminUserClaims = mockAdminUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockJwt = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(MOCK_ISSUER)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY, SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(mockAdminUserClaims)
                .compact();

        Claims mockClaims = Jwts.parserBuilder()
                .setSigningKey(MOCK_PUBLIC_KEY)
                .build()
                .parseClaimsJws(mockJwt)
                .getBody();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        Claims claims = tokenService.getClaims(mockJwt);

        Assertions.assertEquals(mockClaims, claims);

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidJwt_whenJwtParsed_thenReturnUserClaims() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();
        Map<String, Object> mockUserClaims = mockUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockJwt = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(MOCK_ISSUER)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY, SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(mockUserClaims)
                .compact();

        Claims mockClaims = Jwts.parserBuilder()
                .setSigningKey(MOCK_PUBLIC_KEY)
                .build()
                .parseClaimsJws(mockJwt)
                .getBody();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        Claims claims = tokenService.getClaims(mockJwt);

        Assertions.assertEquals(mockClaims, claims);

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidToken_whenTokenParsedAndAdminUserAuthoritiesAdded_thenReturnAuthenticatedUsernamePasswordAuthenticationToken() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().build();
        Map<String, Object> mockAdminUserClaims = mockAdminUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(MOCK_ISSUER)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY, SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(mockAdminUserClaims)
                .compact();

        Claims mockClaims = Jwts.parserBuilder()
                .setSigningKey(MOCK_PUBLIC_KEY)
                .build()
                .parseClaimsJws(mockToken)
                .getBody();

        Jwt mockJwt = new Jwt(
                mockToken,
                Instant.ofEpochSecond(((Double) mockClaims.get(AysTokenClaims.ISSUED_AT.getValue())).intValue()),
                Instant.ofEpochSecond(((Double) mockClaims.get(AysTokenClaims.EXPIRES_AT.getValue())).intValue()),
                Map.of(AysTokenClaims.ALGORITHM.getValue(), SignatureAlgorithm.RS512.getValue()),
                mockClaims
        );

        List<SimpleGrantedAuthority> mockAuthorities = new ArrayList<>();
        mockAuthorities.add(new SimpleGrantedAuthority(AysUserType.ADMIN.name()));

        UsernamePasswordAuthenticationToken mockAuthentication = UsernamePasswordAuthenticationToken
                .authenticated(mockJwt, null, mockAuthorities);


        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(mockToken);

        Assertions.assertEquals(mockAuthentication, authentication);

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidToken_whenTokenParsedAndUserAuthoritiesAdded_thenReturnAuthenticatedUsernamePasswordAuthenticationToken() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();
        Map<String, Object> mockUserClaims = mockUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(MOCK_ISSUER)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY, SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(mockUserClaims)
                .compact();

        Claims mockClaims = Jwts.parserBuilder()
                .setSigningKey(MOCK_PUBLIC_KEY)
                .build()
                .parseClaimsJws(mockToken)
                .getBody();

        Jwt mockJwt = new Jwt(
                mockToken,
                Instant.ofEpochSecond(((Double) mockClaims.get(AysTokenClaims.ISSUED_AT.getValue())).intValue()),
                Instant.ofEpochSecond(((Double) mockClaims.get(AysTokenClaims.EXPIRES_AT.getValue())).intValue()),
                Map.of(AysTokenClaims.ALGORITHM.getValue(), SignatureAlgorithm.RS512.getValue()),
                mockClaims
        );

        List<SimpleGrantedAuthority> mockAuthorities = new ArrayList<>();
        mockAuthorities.add(new SimpleGrantedAuthority(AysUserType.USER.name()));
        List<String> roles = AysListUtil.to(mockClaims.get(AysTokenClaims.ROLES.getValue()), String.class);
        roles.forEach(role -> mockAuthorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken mockAuthentication = UsernamePasswordAuthenticationToken
                .authenticated(mockJwt, null, mockAuthorities);


        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(mockToken);

        Assertions.assertEquals(mockAuthentication, authentication);

        Mockito.verify(tokenConfiguration, Mockito.times(0)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    private static final String MOCK_ISSUER = "AYS";
    private static final Integer MOCK_ACCESS_TOKEN_EXPIRE_MINUTE = 120;
    private static final Integer MOCK_REFRESH_TOKEN_EXPIRE_DAY = 1;
    private static final PrivateKey MOCK_PRIVATE_KEY = KeyConverter.convertPrivateKey("""
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDH+MUShIcE42+v
            eixv8iUJ0+Pd76HWgX2iHhaSftXQs9O5doCOKCMVVkK1H8ORN6ZgDIVIhao5MTCP
            wPvH85YU2Y2ukILfXps2PCdLMQg45b2t8HFqJJAaqzQQ5ACAVWx3hbVWFQs5g7yL
            eB/tagY+QaElDXpNk85Xh6EIIOjQLyqSHa12Fhi9PRaqJM5kC/HQY+E8YtHOAout
            aUG/C78l/sTMg3sPGB/4AcTd3cKiHu3Aauy0pstqdzJ6niMSO1L004MXYaMmBl8I
            g9b60JwVxF2fC1ZHCH+BlckYbQUJ9JCqhr7qnoFbg7ngLe3iqcLmLmPowNm8kpLK
            hkV5AwdPAgMBAAECggEARs+hwW/qe+Gpv+Ksb6u4T+WXcBSWI2ZRPaIX7iI5xqCX
            HbqHxU8TNVAJaSfpUbf6E1L7s3WZlI0FnDIDNofcIl/zWthTb5OJtMfSRj8DoVpB
            M6HMF4EBAmCTnFOQleEp+pz/XI8xHVm3309XRvPfaBZHYN6H64amb7pYXI+CwY0A
            xTAntPO4uKwvrzTc+rHtVLRjA2fRUHZ+QJ+IObXwVxFUFMGS7b98jk2LuzTsQoLx
            dNFaE3XtEpUNyKePnfly2lmBv6obIuxr3Dnppzdye6+u3iJ+TD5uNFAT/ZF24pE/
            oP71rmdPhJCuxK219AJoC15Z5dsBOm1za/synNEnwQKBgQD/VzizN6z1Rt9rk9Vt
            X1apiLpgWhx5USDBcTwya03AtZTrH7hVqkpLFnCOFnyprV3MauztNSpWXOWLljs4
            MtJO+Vfx4YzRS013w8bSp7ZrcFfZNXKa0zkx4rCiaUxzC5LKSN0q3NZxKjC37OYS
            cUHS5knpV19UNSj2sunC3xUj4QKBgQDIfPMnWVgTscqvbdjm/yNwbSYrbYXduuPm
            axrcMe4LiHQ6nabNSM1otkH/tsrQnrhfMDKtsWnDu40Jb8iFvoO8I3/vOhdI3oLG
            oZcPXYljKtrbE0NHt2aRiANto+/GRRy6kH6EKjseerksq4SLLCBbEgjNKrnhViPR
            OXswhK+RLwKBgC5TMrRBG538V7h6v7PyIhTr+3RTpOrVry2pT5SOJzMZPoVR4e2Y
            0ZXB4nXE2qUmEOhvVcDLbnzwqayjeub9QW6WikAV/ahTEyDxYfcB+nSPk0CTE9HH
            FI9aY1Vz6SzOIrmUcpu+KSGq19/mmO8roReUNECjW2Y5ps7rMsHqGznBAoGALzln
            WPRtj64ITQw11Cty4I+FNyOELbdQ6Tx/RjConqTUo93wpVgpsimEIEShP0SzrxA+
            T7WDcSxjSz6+X+qBQzalcucfBvYKndkAKQliTC6TAJln9qOXkF4WWKQN3Yj3/GX+
            twjvhf1oUsJP5SxOrsTvt8wBnrdYlnbQspv+ctcCgYEA4M/Wvbo0MAurrtJ76tfE
            lzvj79/cGU7xNBKODn/PBzgdbE8QnQ5Je+V9ItFPUaT6gUr8zHGyKwcBpZDAsBZk
            fMhHTTgkOI/loUvUo6RuJk+HZm0sWkVk1BvIs478HHgu+8RYVqmkHVhGcFcc3Ghr
            7400PzFngOk2CgDXBijQS+Y=
            -----END PRIVATE KEY-----
            """
    );
    private static final PublicKey MOCK_PUBLIC_KEY = KeyConverter.convertPublicKey("""
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx/jFEoSHBONvr3osb/Il
            CdPj3e+h1oF9oh4Wkn7V0LPTuXaAjigjFVZCtR/DkTemYAyFSIWqOTEwj8D7x/OW
            FNmNrpCC316bNjwnSzEIOOW9rfBxaiSQGqs0EOQAgFVsd4W1VhULOYO8i3gf7WoG
            PkGhJQ16TZPOV4ehCCDo0C8qkh2tdhYYvT0WqiTOZAvx0GPhPGLRzgKLrWlBvwu/
            Jf7EzIN7Dxgf+AHE3d3Coh7twGrstKbLancyep4jEjtS9NODF2GjJgZfCIPW+tCc
            FcRdnwtWRwh/gZXJGG0FCfSQqoa+6p6BW4O54C3t4qnC5i5j6MDZvJKSyoZFeQMH
            TwIDAQAB
            -----END PUBLIC KEY-----
            """);
}