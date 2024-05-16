package org.ays.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
    private String rejectReason;

}
