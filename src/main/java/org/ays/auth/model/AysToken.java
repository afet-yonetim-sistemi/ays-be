package org.ays.auth.model;

import lombok.Builder;
import lombok.Getter;

/**
 * A representation of an access token that includes the access token string, expiration time, and refresh token.
 */
@Getter
@Builder
public class AysToken {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

}
