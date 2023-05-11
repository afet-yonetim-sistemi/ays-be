package com.ays.user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;

public class UserUpdateRequestBuilder extends TestDataBuilder<UserUpdateRequest> {

    public UserUpdateRequestBuilder() {
        super(UserUpdateRequest.class);
    }

    public UserUpdateRequestBuilder withRole(UserRole role) {
        data.setRole(role);
        return this;
    }

    public UserUpdateRequestBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

}
