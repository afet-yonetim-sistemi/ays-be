package org.ays.admin_user.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Email;
import org.ays.common.util.validation.Name;

/**
 * Represents a request to complete an admin-user register application. The request includes fields for the required user
 * information.
 */
@Getter
@Setter
public class AdminRegisterApplicationCompleteRequest {

    @Name
    @NotBlank
    private String firstName;

    @Name
    @NotBlank
    private String lastName;

    @NotBlank
    private String city;

    @NotBlank
    @Email
    private String emailAddress;

    @NotBlank
    private String password;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

}
