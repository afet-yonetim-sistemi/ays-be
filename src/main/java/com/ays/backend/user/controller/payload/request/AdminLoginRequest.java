package com.ays.backend.user.controller.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
