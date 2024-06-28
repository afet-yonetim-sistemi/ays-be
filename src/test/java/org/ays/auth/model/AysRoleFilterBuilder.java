package org.ays.auth.model;

import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class AysRoleFilterBuilder extends TestDataBuilder<AysRoleFilter> {

    public AysRoleFilterBuilder() {
        super(AysRoleFilter.class);
    }

    public AysRoleFilterBuilder withValidValues() {
        return new AysRoleFilterBuilder()
                .withName("role")
                .withStatuses(Set.of(AysRoleStatus.ACTIVE))
                .withInstitutionId(AysRandomUtil.generateUUID());
    }

    public AysRoleFilterBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleFilterBuilder withStatuses(Set<AysRoleStatus> statuses) {
        data.setStatuses(statuses);
        return this;
    }

    public AysRoleFilterBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

}
