package com.ays.admin_user.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserRegisterRequest {

    /* verificationId should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String verificationId;

    /* organizationId should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String organizationId;

    /* username should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String username;

    /* email should be not null and its trimmed length should be greather than zero */
    /* email should be valid */
    @NotBlank
    @Email
    private String email;

    /* password should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String password;

    private AysPhoneNumber phoneNumber;

    /* firstName should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String firstName;

    /* lastName should be not null and its trimmed length should be greather than zero */
    @NotBlank
    private String lastName;

}
