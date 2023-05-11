package com.ays.organization.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;

public class OrganizationEntityBuilder extends TestDataBuilder<OrganizationEntity> {

    public OrganizationEntityBuilder() {
        super(OrganizationEntity.class);
    }

    public OrganizationEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID());
    }

    public OrganizationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

}
