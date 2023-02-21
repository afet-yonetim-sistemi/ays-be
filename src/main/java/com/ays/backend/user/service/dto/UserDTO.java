package com.ays.backend.user.service.dto;

import java.util.Set;

import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.entities.PhoneNumber;
import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * User DTO to perform data transfer from service layer to the api.
 */
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {
    private String username;
    private String userUUID;
    private PhoneNumber phoneNumber;
    private UserStatus userStatus;
    private Set<DeviceType> types;
    private Set<Role> roles;
    private Double latitude;
    private Double longitude;
}
