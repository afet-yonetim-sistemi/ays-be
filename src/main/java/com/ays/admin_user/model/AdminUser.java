package com.ays.admin_user.model;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.BaseDomainModel;
import com.ays.institution.model.Institution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Admin User Domain Model to perform data transfer from service layer to controller
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdminUser extends BaseDomainModel {

    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private AdminUserStatus status;
    private AysPhoneNumber phoneNumber;

    private Institution institution;
}
