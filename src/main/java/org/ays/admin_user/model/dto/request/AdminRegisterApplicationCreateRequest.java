package org.ays.admin_user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.NoSpecialCharacters;

/**
 * Represents a request to register a new administrator user application. The request includes fields for the required
 * application institution id and reason.
 */
@Getter
@Setter
public class AdminRegisterApplicationCreateRequest {

    /**
     * Institution ID for Registering Admin User
     */
    @NotBlank
    private String institutionId;

    /**
     * Reason for Registering Admin User
     */
    @NotBlank
    @Size(min = 40, max = 512)
    @NoSpecialCharacters
    private String reason;

}
