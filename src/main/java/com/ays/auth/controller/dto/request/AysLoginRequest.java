package com.ays.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AysLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
