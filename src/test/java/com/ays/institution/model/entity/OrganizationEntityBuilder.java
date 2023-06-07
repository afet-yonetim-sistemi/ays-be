package com.ays.institution.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.enums.InstitutionStatus;

public class OrganizationEntityBuilder extends TestDataBuilder<InstitutionEntity> {

    public OrganizationEntityBuilder() {
        super(InstitutionEntity.class);
    }

    public OrganizationEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(InstitutionStatus.ACTIVE);
    }

    public OrganizationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public OrganizationEntityBuilder withStatus(InstitutionStatus status) {
        data.setStatus(status);
        return this;
    }

}
