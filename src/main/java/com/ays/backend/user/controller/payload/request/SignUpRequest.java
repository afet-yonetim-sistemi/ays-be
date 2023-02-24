package com.ays.backend.user.controller.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Signup request coming to the API layer
 */
@Data
@Builder
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private int countryCode;

    @NotNull
    private int lineNumber;

    @NotNull
    private int statusId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String email;

    private Long organizationId;
}