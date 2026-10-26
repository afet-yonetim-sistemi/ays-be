package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.NoSpecialCharacters;
import org.ays.common.util.validation.NoSpacesAround;
import org.hibernate.validator.constraints.UUID;

import java.util.Set;

/**
 * Represents a request to register a new admin application. The request includes fields for the required
 * application institution IDs and reason.
 */
@Getter
@Setter
public class AdminRegistrationApplicationCreateRequest {

    /**
     * Legacy single institution ID for registering an admin user.
     */
    @UUID
    private String institutionId;

    /**
     * Institution IDs for registering an admin user in multiple institutions.
     */
    private Set<@NotBlank @UUID String> institutionIds;

    /**
     * Reason for Registering Admin User
     */
    @NotBlank
    @Size(min = 40, max = 512)
    @NoSpecialCharacters
    @NoSpacesAround
    private String reason;

    @AssertTrue(message = "At least one institution ID must be provided")
    @JsonIgnore
    public boolean isInstitutionSelectionValid() {
        return (this.institutionIds != null && !this.institutionIds.isEmpty())
                || (this.institutionId != null && !this.institutionId.isBlank());
    }

}
