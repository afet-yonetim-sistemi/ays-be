package com.ays.user.model;

import com.ays.common.model.AysPhoneNumber;
import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * User DTO to perform data transfer from service layer to the api.
 */
@Getter
@Setter
@Builder
public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private OrganizationEntity organization;
    private UserRole role;
    private UserStatus status;
    private AysPhoneNumber phoneNumber;
    private LocalDateTime lastLoginDate;

}
