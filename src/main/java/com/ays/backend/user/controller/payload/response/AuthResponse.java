package com.ays.backend.user.controller.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
    String message;
    String username;
    String accessToken;
    String refreshToken;
    Long expireDate;
    private List<String> roles;
}
