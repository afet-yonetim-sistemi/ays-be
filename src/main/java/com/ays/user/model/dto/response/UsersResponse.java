package com.ays.user.model.dto.response;

import com.ays.institution.model.dto.response.InstitutionResponse;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * A DTO (Data Transfer Object) representing a list of users in a paginated response.
 */
@Getter
@Builder
public class UsersResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private InstitutionResponse institution;

}
