package org.ays.admin_user.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;

public class AdminRegisterApplicationRejectRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationRejectRequest> {

    public AdminRegisterApplicationRejectRequestBuilder() {
        super(AdminRegistrationApplicationRejectRequest.class);
    }

    public AdminRegisterApplicationRejectRequestBuilder withValidFields() {
        return this
                .withRejectReason(AysRandomTestUtil.generateString(50));
    }


    public AdminRegisterApplicationRejectRequestBuilder withRejectReason(String rejectReason) {
        data.setRejectReason(rejectReason);
        return this;
    }

}
