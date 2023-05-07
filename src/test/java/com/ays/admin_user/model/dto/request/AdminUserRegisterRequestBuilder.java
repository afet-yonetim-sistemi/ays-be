package com.ays.admin_user.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.util.AysTestData;

public class AdminUserRegisterRequestBuilder extends TestDataBuilder<AdminUserRegisterRequest> {

    public AdminUserRegisterRequestBuilder() {
        super(AdminUserRegisterRequest.class);
    }

    public AdminUserRegisterRequestBuilder withValidFields() {
        return this
                .withVerificationId(AysRandomUtil.generateUUID())
                .withOrganizationId(AysRandomUtil.generateUUID())
                .withEmail(AysTestData.VALID_EMAIL)
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build());
    }

    public AdminUserRegisterRequestBuilder withVerificationId(String verificationId) {
        data.setVerificationId(verificationId);
        return this;
    }

    public AdminUserRegisterRequestBuilder withOrganizationId(String organizationId) {
        data.setOrganizationId(organizationId);
        return this;
    }

    public AdminUserRegisterRequestBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminUserRegisterRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

}
