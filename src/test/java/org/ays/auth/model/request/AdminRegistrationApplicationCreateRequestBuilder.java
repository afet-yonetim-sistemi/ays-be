package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class AdminRegistrationApplicationCreateRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationCreateRequest> {

    public AdminRegistrationApplicationCreateRequestBuilder() {
        super(AdminRegistrationApplicationCreateRequest.class);
    }

    public AdminRegistrationApplicationCreateRequestBuilder withValidValues() {
        return this
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withReason(AysRandomUtil.generateText(50));
    }

    public AdminRegistrationApplicationCreateRequestBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitutionIds(null);
        return this;
    }

    public AdminRegistrationApplicationCreateRequestBuilder withInstitutionIds(Set<String> institutionIds) {
        data.setInstitutionIds(institutionIds);
        data.setInstitutionId(null);
        return this;
    }

    public AdminRegistrationApplicationCreateRequestBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }
}
