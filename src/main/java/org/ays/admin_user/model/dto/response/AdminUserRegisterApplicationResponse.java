package org.ays.admin_user.model.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.dto.response.BaseResponse;

/**
 * A DTO (Data Transfer Object) representing an admin user register application in a response.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminUserRegisterApplicationResponse extends BaseResponse {

    private String id;
    private String reason;
    private String rejectReason;
    private AdminUserRegisterApplicationStatus status;
    private Institution institution;
    private AdminUser adminUser;


    /**
     * A DTO (Data Transfer Object) representing an institution in an admin user register application of response.
     */
    @Getter
    @Setter
    public static class Institution {
        private String id;
        private String name;
    }

    /**
     * A DTO (Data Transfer Object) representing an admin users in an admin user register application of response.
     */
    @Getter
    @Setter
    public static class AdminUser {
        private String id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private AysPhoneNumber phoneNumber;
    }

}
