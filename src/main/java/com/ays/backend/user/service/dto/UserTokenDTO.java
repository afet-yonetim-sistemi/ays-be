package com.ays.backend.user.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserTokenDTO {
    private String message;
    private String username;
    private String accessToken;
    private String refreshToken;
    private Long expireDate;
    private Set<String> roles;
}
