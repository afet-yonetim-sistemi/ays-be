package org.ays.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.NoSpacesAround;

/**
 * Represents a request to reject a new admin application. The request includes fields for the required application reject reason.
 */
@Getter
@Setter
public class AdminRegistrationApplicationRejectRequest {

    /**
     * Reject reason for Registering Admin User
     */
    @NotBlank
    @Size(min = 40, max = 512)
    @NoSpacesAround
    private String rejectReason;

}
