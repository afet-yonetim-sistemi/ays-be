package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.enums.AdminRole;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.common.util.AysRandomUtil;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.AysParameterBuilder;
import org.ays.parameter.service.AysParameterService;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.ays.user.repository.UserLoginAttemptRepository;
import org.ays.user.repository.UserRepositoryV2;
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

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractRestControllerTest extends AbstractTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;


    @Autowired
    protected UserRepositoryV2 userRepository;

    @Autowired
    protected UserLoginAttemptRepository loginAttemptRepository;


    protected AysToken mockSuperAdminToken;
    protected AysToken mockAdminUserToken;
    protected AysToken mockUserToken;
    protected AysToken mockSuperAdminTokenV2;
    protected AysToken mockUserTokenV2;
    protected AysToken mockAdminTokenV2;


    @Mock
    private AysTokenConfigurationParameter tokenConfiguration;
    @Mock
    private AysParameterService parameterService;

    @BeforeEach
    @SuppressWarnings("OptionalGetWithoutIsPresent disabled because of the test data is valid")
    public void initializeAuth() {
        Set<AysParameter> parameters = AysParameterBuilder.getParameters();
        Mockito.when(parameterService.getParameters(Mockito.anyString()))
                .thenReturn(parameters);

        this.tokenConfiguration = new AysTokenConfigurationParameter(parameterService);
        this.mockSuperAdminToken = this.generate(new AdminUserEntityBuilder().withValidFields().withRole(AdminRole.SUPER_ADMIN).withInstitutionId(null).build().getClaims());
        this.mockAdminUserToken = this.generate(new AdminUserEntityBuilder().withRole(AdminRole.ADMIN).build().getClaims());
        this.mockUserToken = this.generate(new UserEntityBuilder().build().getClaims());

        final Optional<UserEntityV2> superAdminEntity = userRepository
                .findById(AysValidTestData.SuperAdminUserV2.ID);
        final Optional<UserLoginAttemptEntity> superAdminLoginAttemptEntity = loginAttemptRepository
                .findByUserId(superAdminEntity.get().getId());
        final Claims claimsOfMockSuperAdminToken = superAdminEntity.get()
                .getClaims(superAdminLoginAttemptEntity.get());
        this.mockSuperAdminTokenV2 = this.generate(claimsOfMockSuperAdminToken);

        final Optional<UserEntityV2> adminEntity = userRepository
                .findById(AysValidTestData.AdminV2.ID);
        final Optional<UserLoginAttemptEntity> adminLoginAttemptEntity = loginAttemptRepository
                .findByUserId(adminEntity.get().getId());
        final Claims claimsOfMockAdminToken = adminEntity.get()
                .getClaims(adminLoginAttemptEntity.get());
        this.mockAdminTokenV2 = this.generate(claimsOfMockAdminToken);

        final Optional<UserEntityV2> userEntity = userRepository
                .findById(AysValidTestData.UserV2.ID);
        final Optional<UserLoginAttemptEntity> userLoginAttemptEntity = loginAttemptRepository
                .findByUserId(userEntity.get().getId());
        final Claims claimsOfMockUserToken = userEntity.get()
                .getClaims(userLoginAttemptEntity.get());
        this.mockUserTokenV2 = this.generate(claimsOfMockUserToken);
    }

    private AysToken generate(Map<String, Object> claims) {
        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());
        final String accessToken = Jwts.builder()
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .header()
                .add(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getIssuer())
                .issuedAt(tokenIssuedAt)
                .expiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

}
