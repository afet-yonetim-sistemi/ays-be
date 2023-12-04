package com.ays.user.model.dto.response;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.response.BaseResponse;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.enums.UserSupportStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
