package org.ays.auth.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * A representation of an access token that includes the access token string and refresh token.
 */
@Getter
@Builder
public class AysToken {

    private String accessToken;
    private String refreshToken;


    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Determines whether the provided authorization header is a bearer token by checking whether it starts with the
     * Bearer token prefix.
     *
     * @param authorizationHeader the authorization header value
     * @return true if the authorization header is a bearer token, false otherwise
     */
    public static boolean isBearerToken(String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    /**
     * Extracts the actual JWT token from the provided authorization header by removing the Bearer token prefix.
     *
     * @param authorizationHeader the authorization header value
     * @return the JWT token string without the Bearer token prefix
     */
    public static String getJwt(String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}
