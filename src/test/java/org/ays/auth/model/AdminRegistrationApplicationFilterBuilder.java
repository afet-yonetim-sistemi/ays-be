package org.ays.auth.model;

import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.TestDataBuilder;

import java.util.Set;

public class AdminRegistrationApplicationFilterBuilder extends TestDataBuilder<AdminRegistrationApplicationFilter> {

    public AdminRegistrationApplicationFilterBuilder() {
        super(AdminRegistrationApplicationFilter.class);
        data.setStatuses(null);
    }

    public AdminRegistrationApplicationFilterBuilder withStatus(Set<AdminRegistrationApplicationStatus> statuses) {
        data.setStatuses(statuses);
        return this;
    }

}
