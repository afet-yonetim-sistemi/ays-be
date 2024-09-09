package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

public class AdminRegistrationApplicationRejectRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationRejectRequest> {

    public AdminRegistrationApplicationRejectRequestBuilder() {
        super(AdminRegistrationApplicationRejectRequest.class);
    }

    public AdminRegistrationApplicationRejectRequestBuilder withValidValues() {
        return this
                .withRejectReason(AysRandomUtil.generateText(50));
    }

    public AdminRegistrationApplicationRejectRequestBuilder withRejectReason(String rejectReason) {
        data.setRejectReason(rejectReason);
        return this;
    }

}
