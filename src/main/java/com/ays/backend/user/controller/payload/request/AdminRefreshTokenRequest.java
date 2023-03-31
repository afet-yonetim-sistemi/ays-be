package com.ays.backend.user.controller.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminRefreshTokenRequest {

    @JsonProperty
    @NotBlank
    private String refreshToken;

}
