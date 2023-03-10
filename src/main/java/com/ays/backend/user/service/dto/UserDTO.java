package com.ays.backend.user.service.dto;

import com.ays.backend.user.model.entities.Organization;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User DTO to perform data transfer from service layer to the api.
 */
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Organization organization;
    private UserRole userRole;
    private UserStatus userStatus;
    private int countryCode;
    private int lineNumber;
    private LocalDateTime lastLoginDate;

}
