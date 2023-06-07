package com.ays.institution.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.enums.OrganizationStatus;

public class OrganizationEntityBuilder extends TestDataBuilder<OrganizationEntity> {

    public OrganizationEntityBuilder() {
        super(OrganizationEntity.class);
    }

    public OrganizationEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(OrganizationStatus.ACTIVE);
    }

    public OrganizationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public OrganizationEntityBuilder withStatus(OrganizationStatus status) {
        data.setStatus(status);
        return this;
    }

}
