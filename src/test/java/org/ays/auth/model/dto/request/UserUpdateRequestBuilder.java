package org.ays.auth.model.dto.request;

import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.request.UserUpdateRequest;
import org.ays.common.model.TestDataBuilder;

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
