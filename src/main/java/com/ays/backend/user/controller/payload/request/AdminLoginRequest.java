package com.ays.backend.user.controller.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginRequest {

    private String username;
    private String password;
}
