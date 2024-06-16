package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.enums.UserSupportStatus;

import java.time.LocalDateTime;

/**
 * A DTO (Data Transfer Object) representing a list of users in a paginated response.
 */
@Getter
@Setter
public class UsersResponse {

    private String id;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private UserSupportStatus supportStatus;
    private LocalDateTime createdAt;

}
