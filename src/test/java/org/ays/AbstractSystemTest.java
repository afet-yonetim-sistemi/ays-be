package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.enums.AdminRole;
import org.ays.admin_user.repository.AdminRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.ays.user.model.entity.UserLoginAttemptEntityBuilder;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
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
    protected RoleRepository roleRepository;

    @Autowired
    protected PermissionRepository permissionRepository;

    @Autowired
    protected AdminRegisterApplicationRepository adminRegisterApplicationRepository;

    @Autowired
    protected AdminUserRepository adminUserRepository;

    @Autowired
    protected AysInvalidTokenRepository invalidTokenRepository;


    protected AysToken superAdminToken;
    protected AysToken adminUserToken;
    protected AysToken userToken;
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


        final InstitutionEntity institutionEntity = institutionRepository.findById(AysValidTestData.UserV2.INSTITUTION_ID).get();
        final String userId = AysValidTestData.UserV2.ID;
        final UserLoginAttemptEntity userLoginAttemptEntity = new UserLoginAttemptEntityBuilder()
                .withValidFields()
                .withUserId(userId)
                .build();
        final Claims claimsOfUserV2 = new UserEntityV2Builder()
                .withValidFields()
                .withId(userId)
                .withEmailAddress(AysValidTestData.UserV2.EMAIL_ADDRESS)
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(institutionEntity)
                .build()
                .getClaims(userLoginAttemptEntity);
        this.userTokenV2 = this.generate(claimsOfUserV2);
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
