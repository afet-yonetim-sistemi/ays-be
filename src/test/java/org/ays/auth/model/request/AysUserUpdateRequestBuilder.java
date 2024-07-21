package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;

import java.util.Set;

public class AysUserUpdateRequestBuilder extends TestDataBuilder<AysUserUpdateRequest> {

    public AysUserUpdateRequestBuilder() {
        super(AysUserUpdateRequest.class);
    }

    public AysUserUpdateRequestBuilder withValidValues() {
        return this
                .withEmailAddress("test@afetyonetimsistemi.org")
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withRoleIds(Set.of("67190ae9-e152-4b75-bf74-33b29b7828dc"));
    }

    public AysUserUpdateRequestBuilder withRoleIds(Set<String> roleIds) {
        data.setRoleIds(roleIds);
        return this;
    }

    public AysUserUpdateRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AysUserUpdateRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AysUserUpdateRequestBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysUserUpdateRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AysUserUpdateRequestBuilder withCity(String city) {
        data.setCity(city);
        return this;
    }

}
