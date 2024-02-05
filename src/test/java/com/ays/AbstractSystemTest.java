package com.ays;

import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.config.AysTokenConfigurationParameter;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.repository.AysInvalidTokenRepository;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.super_admin.entity.SuperAdminEntityBuilder;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.repository.UserRepository;
import com.ays.util.AysMockMvc;
import com.ays.util.AysValidTestData;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
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
    protected AssignmentRepository assignmentRepository;

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
        final Map<String, Object> claimsOfSuperAdmin = new SuperAdminEntityBuilder()
                .withId(AysValidTestData.SuperAdminUser.ID)
                .withUsername(AysValidTestData.SuperAdminUser.USERNAME)
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
