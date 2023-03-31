package com.ays.backend.user.model;

import com.ays.backend.user.model.entities.OrganizationEntity;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * User DTO to perform data transfer from service layer to the api.
 */
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
