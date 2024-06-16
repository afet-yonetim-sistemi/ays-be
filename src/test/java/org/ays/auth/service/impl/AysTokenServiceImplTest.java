package org.ays.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.model.entity.UserEntityBuilder;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.enums.AysUserType;
import org.ays.auth.util.KeyConverter;
import org.ays.auth.util.exception.TokenNotValidException;
import org.ays.common.util.AysListUtil;
import org.ays.common.util.AysRandomUtil;
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
        Claims mockAdminUserClaims = mockAdminUserEntity.getClaims();

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

        // Verify
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidUserClaims_whenTokensGenerated_thenReturnAysToken() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();
        Claims mockUserClaims = mockUserEntity.getClaims();

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

        // Verify
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getIssuer();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfiguration, Mockito.times(1)).getPrivateKey();
        Mockito.verify(tokenConfiguration, Mockito.times(0)).getPublicKey();
        Mockito.verifyNoMoreInteractions(tokenConfiguration);
    }

    @Test
    void givenValidAdminUserClaimsAndRefreshToken_whenAccessTokenGenerated_thenReturnAysToken() {
        // Given
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().build();
        Claims mockAdminUserClaims = mockAdminUserEntity.getClaims();

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

        // Verify
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
        Claims mockUserClaims = mockUserEntity.getClaims();

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

        // Verify
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
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(MOCK_ISSUER)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY)
                .compact();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        tokenService.verifyAndValidate(mockJwt);

        // Verify
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

        // Verify
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
        Claims mockAdminUserClaims = mockAdminUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(MOCK_ISSUER)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY)
                .claims(mockAdminUserClaims)
                .compact();

        Claims mockPayload = Jwts.parser()
                .verifyWith(MOCK_PUBLIC_KEY)
                .build()
                .parseSignedClaims(mockToken)
                .getPayload();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        Claims claims = tokenService.getPayload(mockToken);

        Assertions.assertEquals(mockPayload, claims);

        // Verify
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
        Claims mockUserClaims = mockUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(MOCK_ISSUER)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY)
                .claims(mockUserClaims)
                .compact();

        Claims mockClaims = Jwts.parser()
                .verifyWith(MOCK_PUBLIC_KEY)
                .build()
                .parseSignedClaims(mockToken)
                .getPayload();

        // When
        Mockito.when(tokenConfiguration.getPublicKey()).thenReturn(MOCK_PUBLIC_KEY);

        // Then
        Claims claims = tokenService.getPayload(mockToken);

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
        Claims mockAdminUserClaims = mockAdminUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(MOCK_ISSUER)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY)
                .claims(mockAdminUserClaims)
                .compact();

        Jws<Claims> mockClaims = Jwts.parser()
                .verifyWith(MOCK_PUBLIC_KEY)
                .build()
                .parseSignedClaims(mockToken);

        JwsHeader mockHeader = mockClaims.getHeader();
        Claims mockPayload = mockClaims.getPayload();

        Jwt mockJwt = new Jwt(
                mockToken,
                mockPayload.getIssuedAt().toInstant(),
                mockPayload.getExpiration().toInstant(),
                Map.of(
                        AysTokenClaims.TYPE.getValue(), mockHeader.getType(),
                        AysTokenClaims.ALGORITHM.getValue(), mockHeader.getAlgorithm()
                ),
                mockPayload
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
        Claims mockUserClaims = mockUserEntity.getClaims();

        long currentTimeMillis = System.currentTimeMillis();
        String mockToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(MOCK_ISSUER)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(DateUtils.addMinutes(new Date(currentTimeMillis), MOCK_ACCESS_TOKEN_EXPIRE_MINUTE))
                .signWith(MOCK_PRIVATE_KEY)
                .claims(mockUserClaims)
                .compact();

        Jws<Claims> mockClaims = Jwts.parser()
                .verifyWith(MOCK_PUBLIC_KEY)
                .build()
                .parseSignedClaims(mockToken);

        JwsHeader mockHeader = mockClaims.getHeader();
        Claims mockPayload = mockClaims.getPayload();

        Jwt mockJwt = new Jwt(
                mockToken,
                mockPayload.getIssuedAt().toInstant(),
                mockPayload.getExpiration().toInstant(),
                Map.of(
                        AysTokenClaims.TYPE.getValue(), mockHeader.getType(),
                        AysTokenClaims.ALGORITHM.getValue(), mockHeader.getAlgorithm()
                ),
                mockPayload
        );

        List<SimpleGrantedAuthority> mockAuthorities = new ArrayList<>();
        mockAuthorities.add(new SimpleGrantedAuthority(AysUserType.USER.name()));
        List<String> roles = AysListUtil.to(mockPayload.get(AysTokenClaims.ROLES.getValue()), String.class);
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
    private static final PrivateKey MOCK_PRIVATE_KEY = KeyConverter.convertPrivateKey("dkNHbEVaSW9rdGQ1cVBKek03dHJqT2daMmdIbEpUSEw=LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2QUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktZd2dnU2lBZ0VBQW9JQkFRQ0puK0lSRUhmclFsWjEKY0RkbllKcndUL1pvV1hick13aGt1enBXdUJVRDJHdTVteU1DdCtyT0p1S3N4VXFIL2IyZGFlZW81dUdndWY2cwpKcjI5bzZLM1drVVBMdG9uNk5GZUhKZEwwcWs5NmVlWDM5UVhFUHYzdS8yRGJXZTJRWk83aW4wNnhLSTQyOUgwCmg4cjdCT0NzemE1WFpkbjVCUHdmZ0pveUFLbkdGODNPMGpUT0Qzem8zV0VvbGEreW1YYXVoUHorUFl0dndSWncKSkZvQW91MTVZM2Z1SDBna3B4NDdRd0tnVFRwNUl4d1I4RnNxVkVOK1JaNFMzcWhxSktOaGY2SVkwS1RjaHphQQpmelp3QnRON0E1eWI4TURMRnRJTHlmRkdhYW0wbXMzUHJWb0J3LzhzYmxsSGlEMTdOVCtXcmNNVkJRRjJTRFlwCmR3WENMM1JiQWdNQkFBRUNnZ0VBS3FzK2JPbjlOQnlTTDhFRi9IQXdPWGVoUHdNVjRxQWs1dzVCYlNlUHBHeVMKSWE2ZXNUWVNmNjRBczI1THlGUDhXUFMvMVZjWDl6d1RZSTUyWDNoL2QzZHVWK0cvMDRYVWUraERaRWZCSHlnSgpITVpSdklFUWplTmtHejV0WEUyQ255KzEyZVdqSWh2TlFaSmtkV1V5djREWm45RTlQbjYwS0pRM3VtOElOQmt3CjZLNUFIMGxiYy8yMEtFYWZpMUsyMzBTUVFCeFp4bEN6bkNJSmNQYlZGSmxwVUNOM1JkaDFJSkdYZUlLT0NRbjkKQnB4Z2ZKVC9KUFo1cWVXamF1OUYxVWlCbWYyZjFzNnlDSXB2aDVsYUhKMXRuU1NuQXlKalkzYzFFUk1GMHBDSgpwUUtIMlEyaXFEYUxGNGdRQWIyQlRnYm85NWVja1RZaFdPVUlQQUt3UVFLQmdRRGhUbXhvTlQwRXlQWEptVFRPClJvdUNwSW4ycmlldGcwUXdYVVVVSDhMMHNJRUVqRDFpeXp5NVBvNk55OWN1QVNYSXBRM05RaDExZ2k1MTJXNmYKYXIyYWtXdXVRK01ENmRNM3RCa0xQSlJITVQ1SmVJbDBQQTJXc2tvMktTcmZtbUI1TFI5MzdpeFNjZmhvajY4bQpWSHBGQ2FhRDBtdVVzUHZYVVR0ZmhMV1ZZUUtCZ1FDY1g0ekllNS9HSWk3Z0pMZTZZTm5ITmprUmhIb3VreEVECm1HbjB2WlRlUzh1SXZrL1JQL2dMdjIwN3hIOXcwMEc1cS9BbytnWG1DV3pQWlhybmR1SVVZbUxwZ2NKVDh5bWMKQVovSWhsbkU4azBxTHlleWRtWmV0NHNXcXY3NkFseFlhNTdFZ3daaTNMQ1Z3OFBIVnIwb1pHR09vTGd3a3g3QgpRV2V4cXJ4bk93S0JnRXlGNlZYL2R3a1FCRU1Ea1NiYVdQbjNUcENGR0I3YnJhWkxsM0c5VStidHAvUldlV2I3CnBsVTRoUXh1QmxpdXRSbVB6YjlBVEdjajN3blIzcnV3Y2xOMFBzR0NkekZXRXBJaHpqdTl5SkxoaThsQ2NsVVQKTEg1WmNkRXhiRWxqMG81MW4vR0k2RzdjSE1YT3YydGlWK0RvNVRCeW9HMXhLeWczZzlYdWFnb2hBb0dBUkJ0Wgp0ZmdpSHFuRXdOczlLbkFFYWorem0yMlh5YkZFTjh5cVdXNDQ2SmthalBSV3oweU5QSkNqZ3VTU25SRm1EdmhVCklZVEVETzBOOTBhN3dSU0dZMXAydWoxSjVrYUNXUEJjSjNwY251cnBzUFhZMUdHOU5JTzhrS0xwYXZxY1BlYWgKdi9WUlVyM01LMjZZVnJud3FTY1BWbytwcVg1cVpzR1Y2RXYwd3dFQ2dZQkdSckF2cEptVWluazVoc0ZldzRvaAozclMwRTlqQklqekszWU9WWTJJVWxydmJYd2N2OWJ0UGpaVkd4VzZ1UEt0UEtPNTFaUExzRS94YWRiUGllOGhmCmM1aTB3LzVPK24rYlc3TlpmcWwvUFk4OTB5ajZJWHlzZElYMVRwQ1NST1Uzb1ptUWZBTG9WWWNBNHU3QWxpY0sKYnBKbTYwampiMHNNejRIVU9CaVZIUT09Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0KNjNLcEhJM2FXeUtrOHNsVkFiaGY3UjBvcURVeHh5RUk=");
    private static final PublicKey MOCK_PUBLIC_KEY = KeyConverter.convertPublicKey("cExiWUJBSVFTbHVwOXFXSkxSTDhsOGRDVkh1VkxyQ0E=LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFpWi9pRVJCMzYwSldkWEEzWjJDYQo4RS8yYUZsMjZ6TUlaTHM2VnJnVkE5aHJ1WnNqQXJmcXppYmlyTVZLaC8yOW5Xbm5xT2Job0xuK3JDYTl2YU9pCnQxcEZEeTdhSitqUlhoeVhTOUtwUGVubmw5L1VGeEQ3OTd2OWcyMW50a0dUdTRwOU9zU2lPTnZSOUlmSyt3VGcKck0ydVYyWForUVQ4SDRDYU1nQ3B4aGZOenRJMHpnOTg2TjFoS0pXdnNwbDJyb1Q4L2oyTGI4RVdjQ1JhQUtMdAplV04zN2g5SUpLY2VPME1Db0UwNmVTTWNFZkJiS2xSRGZrV2VFdDZvYWlTallYK2lHTkNrM0ljMmdIODJjQWJUCmV3T2NtL0RBeXhiU0M4bnhSbW1wdEpyTno2MWFBY1AvTEc1WlI0ZzllelUvbHEzREZRVUJka2cyS1hjRndpOTAKV3dJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg==SlhaNTl3eFBsWTJEWkNSWDdlZFRlaFhPaFhZRzlBN1Q=");

}
