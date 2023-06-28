package com.ays.user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserSupportStatus;

public class UserSupportStatusUpdateRequestBuilder extends TestDataBuilder<UserSupportStatusUpdateRequest> {

    public UserSupportStatusUpdateRequestBuilder() {
        super(UserSupportStatusUpdateRequest.class);
    }

    public UserSupportStatusUpdateRequestBuilder withSupportStatus(UserSupportStatus supportStatus) {
        data.setSupportStatus(supportStatus);
        return this;
    }
}
