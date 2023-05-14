package com.ays.user.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;

public class UserSaveRequestBuilder extends TestDataBuilder<UserSaveRequest> {

    public UserSaveRequestBuilder() {
        super(UserSaveRequest.class);
    }

    public UserSaveRequestBuilder withValidFields() {
        this.withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build());
        return this;
    }

    public UserSaveRequestBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

}
