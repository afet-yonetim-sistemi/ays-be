package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.util.AysValidTestData;

public class AdminUserRegisterRequestBuilder extends TestDataBuilder<AdminUserRegisterApplicationCompleteRequest> {

    public AdminUserRegisterRequestBuilder() {
        super(AdminUserRegisterApplicationCompleteRequest.class);
    }

    public AdminUserRegisterRequestBuilder withValidFields() {
        return this
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public AdminUserRegisterRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminUserRegisterRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public AdminUserRegisterRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminUserRegisterRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

}
