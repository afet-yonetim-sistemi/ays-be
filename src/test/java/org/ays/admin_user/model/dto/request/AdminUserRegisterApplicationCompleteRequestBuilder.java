package org.ays.admin_user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.util.AysValidTestData;

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
