package org.ays.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.NoSpecialCharacters;

/**
 * Represents a request to register a new admin application. The request includes fields for the required
 * application institution id and reason.
 */
@Getter
@Setter
public class AdminRegistrationApplicationCreateRequest {

    /**
     * Institution ID for Registering Admin User
     */
    @NotBlank
    private String institutionId;

    /**
     * Reason for Registering Admin User
     */
    @NotBlank
    @NoSpecialCharacters
    private String reason;

}
