package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;

public class AdminRegistrationApplicationRejectRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationRejectRequest> {

    public AdminRegistrationApplicationRejectRequestBuilder() {
        super(AdminRegistrationApplicationRejectRequest.class);
    }

    public AdminRegistrationApplicationRejectRequestBuilder withValidValues() {
        return this
                .withRejectReason(AysRandomTestUtil.generateString(50));
    }


    public AdminRegistrationApplicationRejectRequestBuilder withRejectReason(String rejectReason) {
        data.setRejectReason(rejectReason);
        return this;
    }

}
