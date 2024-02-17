package org.ays.admin_user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

/**
 * Admin User Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
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
