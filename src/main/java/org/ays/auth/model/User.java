package org.ays.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

/**
 * User Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
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
