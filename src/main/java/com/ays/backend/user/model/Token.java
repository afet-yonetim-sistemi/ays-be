package com.ays.backend.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpireIn;
    private String refreshToken;

}
