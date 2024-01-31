package com.ays.user.model.dto.response;

import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.enums.UserSupportStatus;
import lombok.Getter;
import lombok.Setter;

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
