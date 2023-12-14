package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.util.AysValidTestData;

public class AdminUserRegisterRequestBuilder extends TestDataBuilder<AdminUserRegisterRequest> {

    public AdminUserRegisterRequestBuilder() {
        super(AdminUserRegisterRequest.class);
    }

    public AdminUserRegisterRequestBuilder withValidFields() {
        return this
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public AdminUserRegisterRequestBuilder withApplicationId(String applicationId) {
        data.setApplicationId(applicationId);
        return this;
    }

    public AdminUserRegisterRequestBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
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
