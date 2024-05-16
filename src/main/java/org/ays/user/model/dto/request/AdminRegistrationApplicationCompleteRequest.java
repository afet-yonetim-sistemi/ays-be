package org.ays.user.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Email;
import org.ays.common.util.validation.Name;

/**
 * Represents a complete registration request for an admin user.
 *
 * <p>
 * Encapsulates the necessary information for completing the admin registration process.
 * </p>
 */
@Getter
@Setter
public class AdminRegistrationApplicationCompleteRequest {

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
