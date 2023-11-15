package com.ays.institution.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.enums.InstitutionStatus;

public class InstitutionEntityBuilder extends TestDataBuilder<InstitutionEntity> {

    public InstitutionEntityBuilder() {
        super(InstitutionEntity.class);
    }

    public InstitutionEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(InstitutionStatus.ACTIVE);
    }

    public InstitutionEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public InstitutionEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public InstitutionEntityBuilder withStatus(InstitutionStatus status) {
        data.setStatus(status);
        return this;
    }

}
