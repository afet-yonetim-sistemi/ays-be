package org.ays.user.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.enums.UserSupportStatus;

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
