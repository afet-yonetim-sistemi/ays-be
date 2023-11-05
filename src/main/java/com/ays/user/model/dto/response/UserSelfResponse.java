package com.ays.user.model.dto.response;

import com.ays.common.model.AysPhoneNumber;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.enums.UserSupportStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * UserSelfResponse is a data transfer object (DTO) that represents user information
 * relevant to the user's own operations.
 */
@Getter
@Setter
public class UserSelfResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private UserSupportStatus supportStatus;
    private AysPhoneNumber phoneNumber;
    private Institution institution;

    /**
     * Institution is a nested class within UserSelfResponse that represents the
     * institution information associated with the user.
     */
    @Getter
    @Setter
    public static class Institution {
        private String name;
    }

}
