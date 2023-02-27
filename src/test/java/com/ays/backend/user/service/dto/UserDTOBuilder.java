package com.ays.backend.user.service.dto;

import com.ays.backend.base.TestDataBuilder;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserStatus;

/**
 * This is a class that can be used to generate test data for the UserDTO class without the need for manual input.
 */
public class UserDTOBuilder extends TestDataBuilder<UserDTO> {

    public UserDTOBuilder() {
        super(UserDTO.class);
    }

    /**
     * To change the status field inside the UserDTO object according to our preference, this method can be used.
     */
    public UserDTOBuilder withStatus(UserStatus status) {
        data.setUserStatus(status);
        return this;
    }

    public UserDTOBuilder withAllInfo(UserDTO userDTO) {
        data.setUsername(userDTO.getUsername());
        data.setFirstName(userDTO.getFirstName());
        data.setLastName(userDTO.getLastName());
        data.setEmail(userDTO.getEmail());
        data.setOrganization(userDTO.getOrganization());
        data.setUserRole(userDTO.getUserRole());
        data.setCountryCode(userDTO.getCountryCode());
        data.setLineNumber(userDTO.getLineNumber());
        data.setLastLoginDate(userDTO.getLastLoginDate());
        return this;
    }
}
