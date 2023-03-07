package com.ays.backend.user.controller.payload.request;

import com.ays.backend.user.model.entities.Organization;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class UpdateUserRequest {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Organization organization;
    private UserRole userRole;
    private UserStatus userStatus;
    private int countryCode;
    private int lineNumber;

}
