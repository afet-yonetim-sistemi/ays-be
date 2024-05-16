package org.ays.admin_user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;

public class AdminRegisterApplicationCreateRequestBuilder extends TestDataBuilder<AdminRegisterApplicationCreateRequest> {

    public AdminRegisterApplicationCreateRequestBuilder() {
        super(AdminRegisterApplicationCreateRequest.class);
    }

    public AdminRegisterApplicationCreateRequestBuilder withValidFields() {
        return this
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withReason(AysRandomTestUtil.generateString(50));
    }

    public AdminRegisterApplicationCreateRequestBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AdminRegisterApplicationCreateRequestBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }
}
