package com.ays.admin_user.model.dto.response;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.institution.model.dto.response.InstitutionResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO (Data Transfer Object) representing a list of admin user register applications in a paginated response.
 */
@Getter
@Setter
public class AdminUserRegisterApplicationsResponse {

    private String id;
    private String reason;
    private AdminUserRegisterApplicationStatus status;
    private InstitutionResponse institution;
    private AdminUser user;
    private String createdUser;
    private LocalDateTime createdAt;


    /**
     * A DTO (Data Transfer Object) representing an admin users in an admin user register application of paginated response.
     */
    @Getter
    @Setter
    public static class AdminUser {
        private String id;
        private String firstName;
        private String lastName;
    }

}
