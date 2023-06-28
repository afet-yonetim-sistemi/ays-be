package com.ays.user.model;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.BaseDomainModel;
import com.ays.institution.model.Institution;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.enums.UserSupportStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * User Domain Model to perform data transfer from service layer to controller
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class User extends BaseDomainModel {

    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private UserSupportStatus supportStatus;
    private AysPhoneNumber phoneNumber;

    private Institution institution;

}
