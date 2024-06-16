package org.ays.admin_user.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.institution.model.response.InstitutionResponse;

/**
 * A DTO (Data Transfer Object) representing a list of admin users in a paginated response.
 */
@Getter
@Setter
@Deprecated(since = "AdminUsersResponse V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public class AdminUsersResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private AdminUserStatus status;

    private InstitutionResponse institution;

}
