package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.enums.AdminRole;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.ays.user.repository.AdminRegistrationApplicationRepository;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.repository.UserLoginAttemptRepository;
import org.ays.user.repository.UserRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractSystemTest extends AbstractTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;

    @Autowired
    protected InstitutionRepository institutionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserRepositoryV2 userRepositoryV2;

    @Autowired
    protected UserLoginAttemptRepository loginAttemptRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected PermissionRepository permissionRepository;

    @Autowired
    protected AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;

    @Autowired
    protected AdminUserRepository adminUserRepository;

    @Autowired
    protected AysInvalidTokenRepository invalidTokenRepository;


    protected AysToken superAdminToken;
    protected AysToken adminUserToken;
    protected AysToken userToken;
    protected AysToken superAdminTokenV2;
    protected AysToken adminTokenV2;
    protected AysToken userTokenV2;


    @Autowired
    private AysTokenConfigurationParameter tokenConfiguration;

    @BeforeEach
    protected void setUp() {
        final Claims claimsOfSuperAdmin = new AdminUserEntityBuilder()
                .withId(AysValidTestData.SuperAdminUser.ID)
                .withUsername(AysValidTestData.SuperAdminUser.USERNAME)
                .withRole(AdminRole.SUPER_ADMIN)
                .withInstitutionId(null)
                .build()
                .getClaims();
        this.superAdminToken = this.generate(claimsOfSuperAdmin);

        final Claims claimsOfAdminUser = new AdminUserEntityBuilder()
                .withId(AysValidTestData.AdminUser.ID)
                .withUsername(AysValidTestData.AdminUser.USERNAME)
                .withEmail(AysValidTestData.AdminUser.EMAIL)
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build()
                .getClaims();
        this.adminUserToken = this.generate(claimsOfAdminUser);

        final Claims claimsOfUser = new UserEntityBuilder()
                .withId(AysValidTestData.User.ID)
                .withUsername(AysValidTestData.User.USERNAME)
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build()
                .getClaims();
        this.userToken = this.generate(claimsOfUser);


        final Optional<UserEntityV2> superAdminEntity = userRepositoryV2
                .findById(AysValidTestData.SuperAdminUserV2.ID);
        final Optional<UserLoginAttemptEntity> superAdminLoginAttemptEntity = loginAttemptRepository
                .findByUserId(superAdminEntity.get().getId());
        final Claims claimsOfMockSuperAdminToken = superAdminEntity.get()
                .getClaims(superAdminLoginAttemptEntity.get());
        this.superAdminTokenV2 = this.generate(claimsOfMockSuperAdminToken);

        final Optional<UserEntityV2> adminEntity = userRepositoryV2
                .findById(AysValidTestData.AdminV2.ID);
        final Optional<UserLoginAttemptEntity> adminLoginAttemptEntity = loginAttemptRepository
                .findByUserId(adminEntity.get().getId());
        final Claims claimsOfMockAdminToken = adminEntity.get()
                .getClaims(adminLoginAttemptEntity.get());
        this.adminTokenV2 = this.generate(claimsOfMockAdminToken);

        final Optional<UserEntityV2> userEntity = userRepositoryV2
                .findById(AysValidTestData.SuperAdminUserV2.ID);
        final Optional<UserLoginAttemptEntity> userLoginAttemptEntity = loginAttemptRepository
                .findByUserId(userEntity.get().getId());
        final Claims claimsOfMockUserToken = userEntity.get()
                .getClaims(userLoginAttemptEntity.get());
        this.userTokenV2 = this.generate(claimsOfMockUserToken);
    }


    protected AysToken generate(Claims claims) {
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
