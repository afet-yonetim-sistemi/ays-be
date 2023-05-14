package com.ays.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * AysTokenResponse is a POJO class that represents a response object containing the access token, its expiration time, and the refresh token.
 * This object is typically returned by an authentication endpoint after the user successfully logs in or refreshes their token.
 * <p>The class uses the Lombok annotations @Data and @Builder to automatically generate getters, setters, and a builder method.
 */
@Data
@Builder
public class AysTokenResponse {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

}
