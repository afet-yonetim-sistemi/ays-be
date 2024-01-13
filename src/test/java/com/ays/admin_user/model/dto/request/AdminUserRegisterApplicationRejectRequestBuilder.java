package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomTestUtil;

public class AdminUserRegisterApplicationRejectRequestBuilder extends TestDataBuilder<AdminUserRegisterApplicationRejectRequest> {

    public AdminUserRegisterApplicationRejectRequestBuilder() {
        super(AdminUserRegisterApplicationRejectRequest.class);
    }

    public AdminUserRegisterApplicationRejectRequestBuilder withValidFields() {
        return this
                .withRejectReason(AysRandomTestUtil.generateString(50));
    }


    public AdminUserRegisterApplicationRejectRequestBuilder withRejectReason(String rejectReason) {
        data.setRejectReason(rejectReason);
        return this;
    }

}
