package org.ays.user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.user.model.request.UserSaveRequest;

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
