package org.ays.user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.user.model.enums.UserSupportStatus;

public class UserSupportStatusUpdateRequestBuilder extends TestDataBuilder<UserSupportStatusUpdateRequest> {

    public UserSupportStatusUpdateRequestBuilder() {
        super(UserSupportStatusUpdateRequest.class);
    }

    public UserSupportStatusUpdateRequestBuilder withSupportStatus(UserSupportStatus supportStatus) {
        data.setSupportStatus(supportStatus);
        return this;
    }

}
