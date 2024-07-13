package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.auth.config.AysTokenConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.port.AysUserReadPort;
import org.ays.common.util.AysRandomUtil;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class AysEndToEndTest extends AysTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;

    protected AysToken superAdminToken;
    protected AysToken adminToken;
    protected AysToken userToken;


    @Autowired
    private AysTokenConfigurationParameter tokenConfiguration;

    @Autowired
    private AysUserReadPort userReadPort;

    @BeforeEach
    @SuppressWarnings("all")
    protected void setUp() {
        final Optional<AysUser> superAdmin = userReadPort.findById(AysValidTestData.SuperAdmin.ID);
        final Claims claimsOfMockSuperAdminToken = superAdmin.get().getClaims();
        this.superAdminToken = this.generate(claimsOfMockSuperAdminToken);

        final Optional<AysUser> admin = userReadPort.findById(AysValidTestData.Admin.ID);
        final Claims claimsOfMockAdminToken = admin.get().getClaims();
        this.adminToken = this.generate(claimsOfMockAdminToken);

        final Optional<AysUser> user = userReadPort.findById(AysValidTestData.SuperAdmin.ID);
        final Claims claimsOfMockUserToken = user.get().getClaims();
        this.userToken = this.generate(claimsOfMockUserToken);
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
