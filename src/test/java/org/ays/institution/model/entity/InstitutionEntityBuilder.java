package org.ays.institution.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.enums.InstitutionStatus;

public class InstitutionEntityBuilder extends TestDataBuilder<InstitutionEntity> {

    public InstitutionEntityBuilder() {
        super(InstitutionEntity.class);
    }

    public InstitutionEntityBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(InstitutionStatus.ACTIVE);
    }

    public InstitutionEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public InstitutionEntityBuilder withStatus(InstitutionStatus status) {
        data.setStatus(status);
        return this;
    }

}
