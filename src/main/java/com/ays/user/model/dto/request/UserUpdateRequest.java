package com.ays.user.model.dto.request;

import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a request object for updating user with its variables.
 */
@Builder
@Data
@AllArgsConstructor
public class UserUpdateRequest {

    private String id;
    private String organizationId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    private UserStatus userStatus;
    private Integer countryCode;
    private Integer lineNumber;

}
