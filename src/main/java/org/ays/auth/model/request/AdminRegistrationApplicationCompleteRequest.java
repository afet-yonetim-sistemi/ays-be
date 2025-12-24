package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.CityName;
import org.ays.common.util.validation.EmailAddress;
import org.ays.common.util.validation.Name;
import org.ays.common.util.validation.Password;

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

    @CityName
    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @EmailAddress
    @NotBlank
    private String emailAddress;

    @NotBlank
    @Password
    private String password;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

}
