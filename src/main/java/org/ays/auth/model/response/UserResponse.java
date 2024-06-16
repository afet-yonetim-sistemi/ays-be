package org.ays.auth.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.response.BaseResponse;

/**
 * This class represents the response for a single user.
 * It includes information such as the user's username, first and last name, role, status, supportStatus and phonenumber.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private UserSupportStatus supportStatus;
    private AysPhoneNumber phoneNumber;

}