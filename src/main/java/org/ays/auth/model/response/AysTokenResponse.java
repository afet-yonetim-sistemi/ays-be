package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * AysTokenResponse is a POJO class that represents a response object containing the access token, its expiration time, and the refresh token.
 * This object is typically returned by an authentication endpoint after the user successfully logs in or refreshes their token.
 */
@Getter
@Setter
public class AysTokenResponse {

    private String accessToken;
    private String refreshToken;

}
