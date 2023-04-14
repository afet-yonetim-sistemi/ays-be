package com.ays.user.model;

import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User DTO to perform data transfer from service layer to the api.
 */
@Getter
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
    private Integer countryCode; // TODO : create PhoneNumber object
    private Integer lineNumber; // TODO : create PhoneNumber object
    private LocalDateTime lastLoginDate;

}
