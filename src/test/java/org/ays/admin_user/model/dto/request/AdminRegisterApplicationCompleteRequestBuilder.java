package org.ays.admin_user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.util.AysValidTestData;

public class AdminRegisterApplicationCompleteRequestBuilder extends TestDataBuilder<AdminRegisterApplicationCompleteRequest> {

    public AdminRegisterApplicationCompleteRequestBuilder() {
        super(AdminRegisterApplicationCompleteRequest.class);
    }

    public AdminRegisterApplicationCompleteRequestBuilder withValidFields() {
        return this
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public AdminRegisterApplicationCompleteRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminRegisterApplicationCompleteRequestBuilder withEmail(String email) {
        data.setEmailAddress(email);
        return this;
    }

    public AdminRegisterApplicationCompleteRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminRegisterApplicationCompleteRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

}
