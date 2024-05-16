package org.ays.admin_user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.util.AysValidTestData;

public class AdminRegistrationApplicationCompleteRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationCompleteRequest> {

    public AdminRegistrationApplicationCompleteRequestBuilder() {
        super(AdminRegistrationApplicationCompleteRequest.class);
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withValidFields() {
        return this
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withEmail(String email) {
        data.setEmailAddress(email);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

}
