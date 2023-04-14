package com.ays.auth.controller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AysTokenResponse {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;
}
