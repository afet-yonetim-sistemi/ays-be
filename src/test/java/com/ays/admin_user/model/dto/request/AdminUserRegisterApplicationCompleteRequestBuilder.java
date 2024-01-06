package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.util.AysValidTestData;

public class AdminUserRegisterApplicationCompleteRequestBuilder extends TestDataBuilder<AdminUserRegisterApplicationCompleteRequest> {

    public AdminUserRegisterApplicationCompleteRequestBuilder() {
        super(AdminUserRegisterApplicationCompleteRequest.class);
    }

    public AdminUserRegisterApplicationCompleteRequestBuilder withValidFields() {
        return this
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public AdminUserRegisterApplicationCompleteRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminUserRegisterApplicationCompleteRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public AdminUserRegisterApplicationCompleteRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminUserRegisterApplicationCompleteRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

}
