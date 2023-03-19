package com.ays.backend.user.controller.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Integer countryCode;

    @NotNull
    private Integer lineNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Long organizationId;

    @NotNull
    private Integer statusValue;

}
