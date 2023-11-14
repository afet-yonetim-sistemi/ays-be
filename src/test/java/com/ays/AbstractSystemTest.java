package com.ays;

import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.auth.config.AysTokenConfigurationParameter;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.common.util.AysRandomUtil;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.AysParameterBuilder;
import com.ays.parameter.service.AysParameterService;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.util.AysTestData;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractSystemTest extends AbstractTestContainerConfiguration {

    @Autowired
    protected MockMvc mockMvc;

    protected AysToken adminUserTokenOne;
    protected AysToken adminUserTokenTwo;
    protected AysToken userTokenOne;
    protected AysToken userTokenTwo;
    protected AysToken userTokenThree;
    protected AysToken userTokenFour;
    protected AysToken userTokenFive;
    protected AysToken userTokenSix;

    @Mock
    private AysTokenConfigurationParameter tokenConfiguration;
    @Mock
    private AysParameterService parameterService;

    @BeforeEach
    public void initializeAuth() {
        Set<AysParameter> parameters = AysParameterBuilder.getParameters();
        Mockito.when(parameterService.getParameters(Mockito.anyString()))
                .thenReturn(parameters);
        this.tokenConfiguration = new AysTokenConfigurationParameter(parameterService);

        final Map<String, Object> claimsOfAdminUserOne = new AdminUserEntityBuilder()
                .withId(AysTestData.AdminUser.VALID_ID_ONE)
                .withUsername(AysTestData.AdminUser.VALID_USERNAME_ONE)
                .withEmail(AysTestData.AdminUser.VALID_EMAIL_ONE)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.adminUserTokenOne = this.generate(claimsOfAdminUserOne);

        final Map<String, Object> claimsOfAdminUserTwo = new AdminUserEntityBuilder()
                .withId(AysTestData.AdminUser.VALID_ID_TWO)
                .withUsername(AysTestData.AdminUser.VALID_USERNAME_TWO)
                .withEmail(AysTestData.AdminUser.VALID_EMAIL_TWO)
                .withInstitutionId(AysTestData.Institution.VALID_ID_TWO)
                .build()
                .getClaims();
        this.adminUserTokenTwo = this.generate(claimsOfAdminUserTwo);


        final Map<String, Object> claimsOfUserOne = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_ONE)
                .withUsername(AysTestData.User.VALID_USERNAME_ONE)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.userTokenOne = this.generate(claimsOfUserOne);

        final Map<String, Object> claimsOfUserTwo = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_TWO)
                .withUsername(AysTestData.User.VALID_USERNAME_TWO)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.userTokenTwo = this.generate(claimsOfUserTwo);

        final Map<String, Object> claimsOfUserThree = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_THREE)
                .withUsername(AysTestData.User.VALID_USERNAME_THREE)
                .withInstitutionId(AysTestData.Institution.VALID_ID_TWO)
                .build()
                .getClaims();
        this.userTokenThree = this.generate(claimsOfUserThree);

        final Map<String, Object> claimsOfUserFour = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_FOUR)
                .withUsername(AysTestData.User.VALID_USERNAME_FOUR)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.userTokenFour = this.generate(claimsOfUserFour);

        final Map<String, Object> claimsOfUserFive = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_FIVE)
                .withUsername(AysTestData.User.VALID_USERNAME_FIVE)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.userTokenFive = this.generate(claimsOfUserFive);

        final Map<String, Object> claimsOfUserSix = new UserEntityBuilder()
                .withId(AysTestData.User.VALID_ID_SIX)
                .withUsername(AysTestData.User.VALID_USERNAME_SIX)
                .withInstitutionId(AysTestData.Institution.VALID_ID_ONE)
                .build()
                .getClaims();
        this.userTokenSix = this.generate(claimsOfUserSix);
    }

    private AysToken generate(Map<String, Object> claims) {
        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());
        final String accessToken = Jwts.builder()
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .setHeaderParam(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .setId(AysRandomUtil.generateUUID())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .setHeaderParam(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
                .claim(AysTokenClaims.USERNAME.getValue(), claims.get(AysTokenClaims.USERNAME.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

}
