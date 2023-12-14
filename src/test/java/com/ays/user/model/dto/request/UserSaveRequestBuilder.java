package com.ays.user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;

public class UserSaveRequestBuilder extends TestDataBuilder<UserSaveRequest> {

    public UserSaveRequestBuilder() {
        super(UserSaveRequest.class);
    }

    public UserSaveRequestBuilder withValidFields() {
        this.withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
        return this;
    }

    public UserSaveRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
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
