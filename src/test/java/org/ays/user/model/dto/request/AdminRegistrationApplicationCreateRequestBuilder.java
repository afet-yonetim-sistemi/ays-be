package org.ays.user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;

public class AdminRegistrationApplicationCreateRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationCreateRequest> {

    public AdminRegistrationApplicationCreateRequestBuilder() {
        super(AdminRegistrationApplicationCreateRequest.class);
    }

    public AdminRegistrationApplicationCreateRequestBuilder withValidFields() {
        return this
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withReason(AysRandomTestUtil.generateString(50));
    }

    public AdminRegistrationApplicationCreateRequestBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AdminRegistrationApplicationCreateRequestBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }
}
