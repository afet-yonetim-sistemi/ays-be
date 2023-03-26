package com.ays.backend.user.controller.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {
    private String message;
    private String username;
    private String accessToken;
    private String refreshToken;
    private Long expireDate;
    private Set<String> roles;
}
