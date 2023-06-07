package com.ays.admin_user.model.dto.response;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.institution.model.dto.response.InstitutionResponse;
import lombok.Builder;
import lombok.Getter;


/**
 * A DTO (Data Transfer Object) representing a list of admin users in a paginated response.
 */
@Getter
@Builder
public class AdminUsersResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private AdminUserStatus status;

    private InstitutionResponse organization;
}
