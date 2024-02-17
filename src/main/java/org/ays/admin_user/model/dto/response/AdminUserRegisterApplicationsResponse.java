package org.ays.admin_user.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;

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
    private Institution institution;
    private AdminUser adminUser;
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

    /**
     * A DTO (Data Transfer Object) representing an institution in an admin user register application of paginated response.
     */
    @Getter
    @Setter
    public static class Institution {
        private String id;
        private String name;
    }

}
