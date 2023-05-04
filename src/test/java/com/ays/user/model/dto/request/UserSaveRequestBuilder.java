package com.ays.user.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class UserSaveRequestBuilder extends TestDataBuilder<UserSaveRequest> {

    public UserSaveRequestBuilder() {
        super(UserSaveRequest.class);
    }

    public UserSaveRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public UserSaveRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }
}
