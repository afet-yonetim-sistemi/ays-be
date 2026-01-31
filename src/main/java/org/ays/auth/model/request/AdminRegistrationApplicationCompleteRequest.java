package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.CityForm;
import org.ays.common.util.validation.EmailAddress;
import org.ays.common.util.validation.NameForm;
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

    @NameForm
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NameForm
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @CityForm
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
