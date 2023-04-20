package com.ays.user.model.dto.response;

import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Builder;

@Builder
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private OrganizationEntity organization;
    private UserRole role;
    private UserStatus status;

}
