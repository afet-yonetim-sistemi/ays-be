package org.ays.auth.model.dto.request;

import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.auth.model.request.UserSupportStatusUpdateRequest;
import org.ays.common.model.TestDataBuilder;

public class UserSupportStatusUpdateRequestBuilder extends TestDataBuilder<UserSupportStatusUpdateRequest> {

    public UserSupportStatusUpdateRequestBuilder() {
        super(UserSupportStatusUpdateRequest.class);
    }

    public UserSupportStatusUpdateRequestBuilder withSupportStatus(UserSupportStatus supportStatus) {
        data.setSupportStatus(supportStatus);
        return this;
    }

}
