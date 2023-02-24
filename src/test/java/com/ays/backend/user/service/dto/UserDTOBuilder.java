package com.ays.backend.user.service.dto;

import com.ays.backend.base.TestDataBuilder;
import com.ays.backend.user.model.enums.UserStatus;

public class UserDTOBuilder extends TestDataBuilder<UserDTO> {

    public UserDTOBuilder() {
        super(UserDTO.class);
    }

    public UserDTOBuilder withStatus(UserStatus status) {
        data.setUserStatus(status);
        return this;
    }
}
