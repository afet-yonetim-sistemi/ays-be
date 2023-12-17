package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomTestUtil;
import com.ays.common.util.AysRandomUtil;

public class AdminUserRegisterApplicationCreateRequestBuilder extends TestDataBuilder<AdminUserRegisterApplicationCreateRequest> {

    public AdminUserRegisterApplicationCreateRequestBuilder() {
        super(AdminUserRegisterApplicationCreateRequest.class);
    }

    public AdminUserRegisterApplicationCreateRequestBuilder withValidFields() {
        return this
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withReason(AysRandomTestUtil.generateString(50));
    }

    public AdminUserRegisterApplicationCreateRequestBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AdminUserRegisterApplicationCreateRequestBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }
}
