package org.ays.user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;

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
