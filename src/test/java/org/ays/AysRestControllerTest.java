package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.port.AysUserReadPort;
import org.ays.common.util.AysRandomUtil;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.service.AysParameterService;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"default", "test"})
@ExtendWith(MockitoExtension.class)
public abstract class AysRestControllerTest extends AysTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;

    @Autowired
    protected AysUserReadPort userReadPort;


    protected AysToken mockSuperAdminToken;
    protected AysToken mockAdminToken;
    protected AysToken mockUserToken;


    @Mock
    private AysApplicationConfigurationParameter applicationConfigurationParameter;

    @Mock
    private AysParameterService parameterService;

    @BeforeEach
    @SuppressWarnings("OptionalGetWithoutIsPresent disabled because of the test data is valid")
    public void initializeAuth() {
        Set<AysParameter> parameters = Set.of(
                AysParameter.builder()
                        .name("AUTH_TOKEN_PRIVATE_KEY")
                        .definition("dkNHbEVaSW9rdGQ1cVBKek03dHJqT2daMmdIbEpUSEw=LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2QUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktZd2dnU2lBZ0VBQW9JQkFRQ0puK0lSRUhmclFsWjEKY0RkbllKcndUL1pvV1hick13aGt1enBXdUJVRDJHdTVteU1DdCtyT0p1S3N4VXFIL2IyZGFlZW81dUdndWY2cwpKcjI5bzZLM1drVVBMdG9uNk5GZUhKZEwwcWs5NmVlWDM5UVhFUHYzdS8yRGJXZTJRWk83aW4wNnhLSTQyOUgwCmg4cjdCT0NzemE1WFpkbjVCUHdmZ0pveUFLbkdGODNPMGpUT0Qzem8zV0VvbGEreW1YYXVoUHorUFl0dndSWncKSkZvQW91MTVZM2Z1SDBna3B4NDdRd0tnVFRwNUl4d1I4RnNxVkVOK1JaNFMzcWhxSktOaGY2SVkwS1RjaHphQQpmelp3QnRON0E1eWI4TURMRnRJTHlmRkdhYW0wbXMzUHJWb0J3LzhzYmxsSGlEMTdOVCtXcmNNVkJRRjJTRFlwCmR3WENMM1JiQWdNQkFBRUNnZ0VBS3FzK2JPbjlOQnlTTDhFRi9IQXdPWGVoUHdNVjRxQWs1dzVCYlNlUHBHeVMKSWE2ZXNUWVNmNjRBczI1THlGUDhXUFMvMVZjWDl6d1RZSTUyWDNoL2QzZHVWK0cvMDRYVWUraERaRWZCSHlnSgpITVpSdklFUWplTmtHejV0WEUyQ255KzEyZVdqSWh2TlFaSmtkV1V5djREWm45RTlQbjYwS0pRM3VtOElOQmt3CjZLNUFIMGxiYy8yMEtFYWZpMUsyMzBTUVFCeFp4bEN6bkNJSmNQYlZGSmxwVUNOM1JkaDFJSkdYZUlLT0NRbjkKQnB4Z2ZKVC9KUFo1cWVXamF1OUYxVWlCbWYyZjFzNnlDSXB2aDVsYUhKMXRuU1NuQXlKalkzYzFFUk1GMHBDSgpwUUtIMlEyaXFEYUxGNGdRQWIyQlRnYm85NWVja1RZaFdPVUlQQUt3UVFLQmdRRGhUbXhvTlQwRXlQWEptVFRPClJvdUNwSW4ycmlldGcwUXdYVVVVSDhMMHNJRUVqRDFpeXp5NVBvNk55OWN1QVNYSXBRM05RaDExZ2k1MTJXNmYKYXIyYWtXdXVRK01ENmRNM3RCa0xQSlJITVQ1SmVJbDBQQTJXc2tvMktTcmZtbUI1TFI5MzdpeFNjZmhvajY4bQpWSHBGQ2FhRDBtdVVzUHZYVVR0ZmhMV1ZZUUtCZ1FDY1g0ekllNS9HSWk3Z0pMZTZZTm5ITmprUmhIb3VreEVECm1HbjB2WlRlUzh1SXZrL1JQL2dMdjIwN3hIOXcwMEc1cS9BbytnWG1DV3pQWlhybmR1SVVZbUxwZ2NKVDh5bWMKQVovSWhsbkU4azBxTHlleWRtWmV0NHNXcXY3NkFseFlhNTdFZ3daaTNMQ1Z3OFBIVnIwb1pHR09vTGd3a3g3QgpRV2V4cXJ4bk93S0JnRXlGNlZYL2R3a1FCRU1Ea1NiYVdQbjNUcENGR0I3YnJhWkxsM0c5VStidHAvUldlV2I3CnBsVTRoUXh1QmxpdXRSbVB6YjlBVEdjajN3blIzcnV3Y2xOMFBzR0NkekZXRXBJaHpqdTl5SkxoaThsQ2NsVVQKTEg1WmNkRXhiRWxqMG81MW4vR0k2RzdjSE1YT3YydGlWK0RvNVRCeW9HMXhLeWczZzlYdWFnb2hBb0dBUkJ0Wgp0ZmdpSHFuRXdOczlLbkFFYWorem0yMlh5YkZFTjh5cVdXNDQ2SmthalBSV3oweU5QSkNqZ3VTU25SRm1EdmhVCklZVEVETzBOOTBhN3dSU0dZMXAydWoxSjVrYUNXUEJjSjNwY251cnBzUFhZMUdHOU5JTzhrS0xwYXZxY1BlYWgKdi9WUlVyM01LMjZZVnJud3FTY1BWbytwcVg1cVpzR1Y2RXYwd3dFQ2dZQkdSckF2cEptVWluazVoc0ZldzRvaAozclMwRTlqQklqekszWU9WWTJJVWxydmJYd2N2OWJ0UGpaVkd4VzZ1UEt0UEtPNTFaUExzRS94YWRiUGllOGhmCmM1aTB3LzVPK24rYlc3TlpmcWwvUFk4OTB5ajZJWHlzZElYMVRwQ1NST1Uzb1ptUWZBTG9WWWNBNHU3QWxpY0sKYnBKbTYwampiMHNNejRIVU9CaVZIUT09Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0KNjNLcEhJM2FXeUtrOHNsVkFiaGY3UjBvcURVeHh5RUk=").build(),
                AysParameter.builder()
                        .name("AUTH_TOKEN_PUBLIC_KEY")
                        .definition("cExiWUJBSVFTbHVwOXFXSkxSTDhsOGRDVkh1VkxyQ0E=LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFpWi9pRVJCMzYwSldkWEEzWjJDYQo4RS8yYUZsMjZ6TUlaTHM2VnJnVkE5aHJ1WnNqQXJmcXppYmlyTVZLaC8yOW5Xbm5xT2Job0xuK3JDYTl2YU9pCnQxcEZEeTdhSitqUlhoeVhTOUtwUGVubmw5L1VGeEQ3OTd2OWcyMW50a0dUdTRwOU9zU2lPTnZSOUlmSyt3VGcKck0ydVYyWForUVQ4SDRDYU1nQ3B4aGZOenRJMHpnOTg2TjFoS0pXdnNwbDJyb1Q4L2oyTGI4RVdjQ1JhQUtMdAplV04zN2g5SUpLY2VPME1Db0UwNmVTTWNFZkJiS2xSRGZrV2VFdDZvYWlTallYK2lHTkNrM0ljMmdIODJjQWJUCmV3T2NtL0RBeXhiU0M4bnhSbW1wdEpyTno2MWFBY1AvTEc1WlI0ZzllelUvbHEzREZRVUJka2cyS1hjRndpOTAKV3dJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg==SlhaNTl3eFBsWTJEWkNSWDdlZFRlaFhPaFhZRzlBN1Q=").build(),
                AysParameter.builder()
                        .name("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                        .definition("120")
                        .build(),
                AysParameter.builder()
                        .name("AUTH_REFRESH_TOKEN_EXPIRE_DAY")
                        .definition("1")
                        .build(),
                AysParameter.builder()
                        .name("AUTH_LOGIN_MAX_TRY_COUNT")
                        .definition("3")
                        .build()
        );
        Mockito.when(parameterService.findAll(Mockito.anyString()))
                .thenReturn(parameters);

        this.applicationConfigurationParameter = new AysApplicationConfigurationParameter(parameterService);

        final Optional<AysUser> superAdmin = userReadPort.findById(AysValidTestData.SuperAdmin.ID);
        final Claims claimsOfMockSuperAdminToken = superAdmin.get().getClaims();
        this.mockSuperAdminToken = this.generate(claimsOfMockSuperAdminToken);

        final Optional<AysUser> admin = userReadPort.findById(AysValidTestData.Admin.ID);
        final Claims claimsOfMockAdminToken = admin.get().getClaims();
        this.mockAdminToken = this.generate(claimsOfMockAdminToken);

        final Optional<AysUser> user = userReadPort.findById(AysValidTestData.User.ID);
        final Claims claimsOfMockUserToken = user.get().getClaims();
        this.mockUserToken = this.generate(claimsOfMockUserToken);
    }

    private AysToken generate(Map<String, Object> claims) {
        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), applicationConfigurationParameter.getAccessTokenExpireMinute());
        final String accessToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(applicationConfigurationParameter.getTokenIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(applicationConfigurationParameter.getTokenPrivateKey())
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), applicationConfigurationParameter.getRefreshTokenExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(applicationConfigurationParameter.getTokenIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(refreshTokenExpiresAt)
                .signWith(applicationConfigurationParameter.getTokenPrivateKey())
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
