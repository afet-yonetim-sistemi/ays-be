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
