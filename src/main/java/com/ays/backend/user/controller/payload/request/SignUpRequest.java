package com.ays.backend.user.controller.payload.request;

import java.util.Set;

import com.ays.backend.user.model.entities.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private PhoneNumber phoneNumber;

    @NotBlank
    private Double latitude;

    @NotBlank
    private Double longitude;

    @NotBlank
    private String status;

    @NotBlank
    private Set<String> types;

    @NotBlank
    private Set<String> roles;
}
