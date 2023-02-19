package com.ays.backend.user.payload.request;

import com.ays.backend.user.model.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
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
