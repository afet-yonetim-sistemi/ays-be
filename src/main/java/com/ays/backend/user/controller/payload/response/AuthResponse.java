package com.ays.backend.user.controller.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String message;
    String accessToken;
}
