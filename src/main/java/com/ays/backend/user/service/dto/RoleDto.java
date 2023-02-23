package com.ays.backend.user.service.dto;

import com.ays.backend.user.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * UserType DTO to perform data transfer from service layer to the api.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleDto {
    UserRole userRole;

    public RoleDto(UserRole userRole) {
        this.userRole = userRole;
    }
}
