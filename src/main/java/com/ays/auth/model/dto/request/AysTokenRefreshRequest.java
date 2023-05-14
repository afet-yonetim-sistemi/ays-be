package com.ays.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AysTokenRefreshRequest is a POJO class that represents a request object containing a refresh token.
 * This object is typically used to send a refresh token to an authentication endpoint to obtain a new access token.
 * <p>The class uses the Lombok annotations @Getter, @Builder, @NoArgsConstructor, and @AllArgsConstructor to automatically generate getters, a builder method, and constructors.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AysTokenRefreshRequest {

    @NotBlank
    private String refreshToken;

}
