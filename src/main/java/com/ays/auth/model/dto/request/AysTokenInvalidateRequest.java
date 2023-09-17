package com.ays.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * AysTokenInvalidateRequest is a request class used for invalidating a token.
 * It includes a refresh token field.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AysTokenInvalidateRequest {

    @NotBlank
    private String refreshToken;

}
