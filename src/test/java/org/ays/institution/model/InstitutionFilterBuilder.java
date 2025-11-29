package org.ays.institution.model;

import org.ays.common.model.TestDataBuilder;
import org.ays.institution.model.enums.InstitutionStatus;

import java.util.Set;

public class InstitutionFilterBuilder extends TestDataBuilder<InstitutionFilter> {

    public InstitutionFilterBuilder() {
        super(InstitutionFilter.class);
    }

    public InstitutionFilterBuilder withValidValues() {
        return new InstitutionFilterBuilder()
                .withName("Dernek")
                .withStatuses(Set.of(InstitutionStatus.ACTIVE));
    }

    public InstitutionFilterBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public InstitutionFilterBuilder withStatuses(Set<InstitutionStatus> statuses) {
        data.setStatuses(statuses);
        return this;
    }
}
