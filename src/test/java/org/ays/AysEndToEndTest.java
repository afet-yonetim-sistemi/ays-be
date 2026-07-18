package org.ays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.enums.AysTokenVariant;
import org.ays.auth.port.AysUserReadPort;
import org.ays.common.util.AysRandomUtil;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"default", "test"})
@ExtendWith(MockitoExtension.class)
public abstract class AysEndToEndTest extends AysTestContainerConfiguration {

    @Autowired
    protected AysMockMvc aysMockMvc;

    protected AysToken superAdminToken;
    protected AysToken adminToken;
    protected AysToken userToken;


    @Autowired
    private AysApplicationConfigurationParameter tokenConfiguration;

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


    private static final String TOKEN_TYPE = "JWT";

    protected AysToken generate(Claims claims) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());
        final String accessToken = this.initializeTokenBuilder(currentTimeMillis, AysTokenVariant.ACCESS)
                .expiration(accessTokenExpiresAt)
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireMinute());
        final String refreshToken = this.initializeTokenBuilder(currentTimeMillis, AysTokenVariant.REFRESH)
                .expiration(refreshTokenExpiresAt)
                .claim(AysTokenClaims.USER_ID.getValue(), claims.get(AysTokenClaims.USER_ID.getValue()))
                .compact();

        return AysToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private JwtBuilder initializeTokenBuilder(final long currentTimeMillis, final AysTokenVariant variant) {
        return Jwts.builder()
                .header()
                .type(TOKEN_TYPE)
                .add(AysTokenClaims.VARIANT.getValue(), variant)
                .and()
                .id(AysRandomUtil.generateUUID())
                .issuer(tokenConfiguration.getTokenIssuer())
                .issuedAt(new Date(currentTimeMillis))
                .signWith(tokenConfiguration.getTokenPrivateKey());
    }

    protected Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(tokenConfiguration.getTokenPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
