package org.ays;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.enums.AdminRole;
import org.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.repository.AysInvalidTokenRepository;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.repository.UserRepository;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Date;
import java.util.Map;

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
    protected AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;

    @Autowired
    protected AdminUserRepository adminUserRepository;

    @Autowired
    protected AysInvalidTokenRepository invalidTokenRepository;


    protected AysToken superAdminToken;
    protected AysToken adminUserToken;
    protected AysToken userToken;


    @Autowired
    private AysTokenConfigurationParameter tokenConfiguration;

    @BeforeEach
    protected void setUp() {
        final Map<String, Object> claimsOfSuperAdmin = new AdminUserEntityBuilder()
                .withId(AysValidTestData.SuperAdminUser.ID)
                .withUsername(AysValidTestData.SuperAdminUser.USERNAME)
                .withRole(AdminRole.SUPER_ADMIN)
                .withInstitutionId(null)
                .build()
                .getClaims();
        this.superAdminToken = this.generate(claimsOfSuperAdmin);

        final Map<String, Object> claimsOfAdminUser = new AdminUserEntityBuilder()
                .withId(AysValidTestData.AdminUser.ID)
                .withUsername(AysValidTestData.AdminUser.USERNAME)
                .withEmail(AysValidTestData.AdminUser.EMAIL)
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build()
                .getClaims();
        this.adminUserToken = this.generate(claimsOfAdminUser);

        final Map<String, Object> claimsOfUser = new UserEntityBuilder()
                .withId(AysValidTestData.User.ID)
                .withUsername(AysValidTestData.User.USERNAME)
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build()
                .getClaims();
        this.userToken = this.generate(claimsOfUser);
    }


    protected AysToken generate(Map<String, Object> claims) {
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
