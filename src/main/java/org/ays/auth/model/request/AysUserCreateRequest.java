package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Email;
import org.ays.common.util.validation.Name;
import org.hibernate.validator.constraints.UUID;

import java.util.Set;

/**
 * Represents a request for creating a new user in the system.
 * This class contains all the necessary details required to create a user, including their personal information,
 * contact details, and roles. The fields are validated to ensure they meet the required constraints.
 * <p>
 * This class uses various annotations for validation to ensure the data integrity and consistency.
 * </p>
 */
@Getter
@Setter
public class AysUserCreateRequest {

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @Email
    @NotBlank
    @Size(min = 2, max = 255)
    private String emailAddress;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @NotEmpty
    private Set<@NotBlank @UUID String> roleIds;

}
