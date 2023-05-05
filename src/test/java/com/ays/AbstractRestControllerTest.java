package com.ays;

import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.auth.config.AysTokenConfiguration;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.AysParameterBuilder;
import com.ays.parameter.service.AysParameterService;
import com.ays.user.model.entity.UserEntityBuilder;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public abstract class AbstractRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    protected AysToken mockAdminUserToken;
    protected AysToken mockUserToken;


    @Mock
    private AysTokenConfiguration tokenConfiguration;
    @Mock
    private AysParameterService parameterService;

    @BeforeEach
    public void generateToken() {
        Set<AysParameter> parameters = AysParameterBuilder.getParameters();
        Mockito.when(parameterService.getParameters(Mockito.anyString()))
                .thenReturn(parameters);
        this.tokenConfiguration = new AysTokenConfiguration(parameterService);
        this.mockAdminUserToken = this.generate(new AdminUserEntityBuilder().build().getClaims());
        this.mockUserToken = this.generate(new UserEntityBuilder().build().getClaims());
    }

    private AysToken generate(Map<String, Object> claims) {
        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessExpireMinute());
        final String accessToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .addClaims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshExpireDay());
        final JwtBuilder refreshTokenBuilder = Jwts.builder();
        final String refreshToken = refreshTokenBuilder
                .setId(UUID.randomUUID().toString())
                .setIssuer(tokenConfiguration.getIssuer())
                .setIssuedAt(tokenIssuedAt)
                .setExpiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey(), SignatureAlgorithm.RS512)
                .claim(AysTokenClaims.TYPE.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .claim(AysTokenClaims.USERNAME.getValue(), claims.get(AysTokenClaims.USERNAME.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

}
