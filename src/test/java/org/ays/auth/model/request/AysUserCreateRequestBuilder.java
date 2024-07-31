package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class AysUserCreateRequestBuilder extends TestDataBuilder<AysUserCreateRequest> {

    public AysUserCreateRequestBuilder() {
        super(AysUserCreateRequest.class);
    }

    public AysUserCreateRequestBuilder withValidValues() {
        return this
                .withFirstName("John")
                .withLastName("Doe")
                .withEmailAddress(AysRandomUtil.generateText(8).concat("@afetyonetimsistemi.org"))
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withCity("Istanbul")
                .withRoleIds(Set.of("b99c9d41-9bf2-4120-a453-1b648197460a"));
    }

    public AysUserCreateRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AysUserCreateRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AysUserCreateRequestBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysUserCreateRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AysUserCreateRequestBuilder withCity(String city) {
        data.setCity(city);
        return this;
    }

    public AysUserCreateRequestBuilder withRoleIds(Set<String> roleIds) {
        data.setRoleIds(roleIds);
        return this;
    }

}
