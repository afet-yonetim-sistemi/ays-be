package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.CityName;
import org.ays.common.util.validation.EmailAddress;
import org.ays.common.util.validation.Name;
import org.hibernate.validator.constraints.UUID;

import java.util.Set;

/**
 * Request object for updating a user's details.
 * <p>
 * This class encapsulates the data required to update an existing user's details.
 * Each field is validated to ensure it meets the necessary requirements.
 */
@Getter
@Setter
public class AysUserUpdateRequest {

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @EmailAddress
    @NotBlank
    private String emailAddress;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @CityName
    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @NotEmpty
    private Set<@NotBlank @UUID String> roleIds;

}
