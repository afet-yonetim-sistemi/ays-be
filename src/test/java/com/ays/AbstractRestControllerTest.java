package com.ays;

import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.auth.config.AysTokenConfigurationParameter;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.common.util.AysRandomUtil;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.AysParameterBuilder;
import com.ays.parameter.service.AysParameterService;
import com.ays.super_admin.entity.SuperAdminEntityBuilder;
import com.ays.user.model.entity.UserEntityBuilder;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
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

import java.util.Date;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractRestControllerTest extends AbstractTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;

    protected AysToken mockSuperAdminToken;
    protected AysToken mockAdminUserToken;
    protected AysToken mockUserToken;


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
        this.mockSuperAdminToken = this.generate(new SuperAdminEntityBuilder().withValidFields().build().getClaims());
        this.mockAdminUserToken = this.generate(new AdminUserEntityBuilder().build().getClaims());
        this.mockUserToken = this.generate(new UserEntityBuilder().build().getClaims());
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
