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
 * information, such as the user's username, email, password, and phone number, as well as their first and last name.
 */
@Getter
@Setter
public class AdminUserRegisterApplicationCompleteRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @NotBlank
    @Name
    private String firstName;

    @NotBlank
    @Name
    private String lastName;

}
