package com.ays.backend.user.service.dto;

import com.ays.backend.base.TestDataBuilder;
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
}
