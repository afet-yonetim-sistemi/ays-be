package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.util.AysValidTestData;

public class AdminRegistrationApplicationCompleteRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationCompleteRequest> {

    public AdminRegistrationApplicationCompleteRequestBuilder() {
        super(AdminRegistrationApplicationCompleteRequest.class);
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withValidValues() {
        return this
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build());
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withEmail(String email) {
        data.setEmailAddress(email);
        return this;
    }

    public AdminRegistrationApplicationCompleteRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

}
