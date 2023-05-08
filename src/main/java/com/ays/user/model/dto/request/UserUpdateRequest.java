package com.ays.user.model.dto.request;

import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a request object for updating user with its variables.
 * <p>
 * This class provides getters and setters for the id, organization id, username, first name,
 * last name, email, user role, user status, country code and line number fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to updating a user, allowing for easy
 * transfer of the data between different layers of the application.
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
