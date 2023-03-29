package com.ays.backend.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTokenDTO {
    private String username;
    private String accessToken;
    private String refreshToken;
    private Long expireDate;
}
