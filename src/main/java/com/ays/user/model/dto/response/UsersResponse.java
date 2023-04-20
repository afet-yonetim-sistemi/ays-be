package com.ays.user.model.dto.response;

import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsersResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private OrganizationEntity organization;
    private UserRole role;
    private UserStatus status;

}
