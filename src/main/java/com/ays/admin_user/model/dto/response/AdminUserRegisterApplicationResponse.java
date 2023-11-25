package com.ays.admin_user.model.dto.response;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.response.BaseResponse;
import com.ays.institution.model.dto.response.InstitutionResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    private InstitutionResponse institution;
    private AdminUser user;


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
