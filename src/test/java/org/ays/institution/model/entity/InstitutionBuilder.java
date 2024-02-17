package org.ays.institution.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;

public class InstitutionBuilder extends TestDataBuilder<Institution> {

    public InstitutionBuilder() {
        super(Institution.class);
    }

    public InstitutionBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(InstitutionStatus.ACTIVE);
    }

    public InstitutionBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public InstitutionBuilder withStatus(InstitutionStatus status) {
        data.setStatus(status);
        return this;
    }
}

