package com.ays.user.model.dto.response;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.response.BaseResponse;
import com.ays.organization.model.dto.response.OrganizationResponse;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the response for a single user.
 * It includes information such as the user's username, first and last name, email, organization, role and status.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserResponse extends BaseResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private AysPhoneNumber phoneNumber;

    private OrganizationResponse organization;

}
