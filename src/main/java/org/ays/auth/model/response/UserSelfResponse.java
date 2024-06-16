package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.common.model.AysPhoneNumber;

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
