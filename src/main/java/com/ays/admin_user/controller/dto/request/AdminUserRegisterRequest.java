package com.ays.admin_user.controller.dto.request;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserRegisterRequest {

    @NotBlank
    private String verificationId;

    @NotBlank
    private String organizationId;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private AysPhoneNumber phoneNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}
