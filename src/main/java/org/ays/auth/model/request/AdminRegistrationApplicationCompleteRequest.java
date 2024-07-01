package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
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
    @Size(min = 2, max = 100)
    private String firstName;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @Email
    @NotBlank
    @Size(min = 2, max = 255)
    private String emailAddress;

    @NotBlank
    private String password;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

}
