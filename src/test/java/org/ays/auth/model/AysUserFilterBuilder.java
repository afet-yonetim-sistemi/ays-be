package org.ays.auth.model;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class AysUserFilterBuilder extends TestDataBuilder<AysUserFilter> {

    public AysUserFilterBuilder() {
        super(AysUserFilter.class);
    }

    public AysUserFilterBuilder withValidValues() {
        return new AysUserFilterBuilder()
                .withFirstName("user")
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .withInstitutionId(AysRandomUtil.generateUUID());
    }

    public AysUserFilterBuilder withFirstName(String name) {
        data.setFirstName(name);
        return this;
    }

    public AysUserFilterBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AysUserFilterBuilder withCity(String city) {
        data.setCity(city);
        return this;
    }

    public AysUserFilterBuilder withStatuses(Set<AysUserStatus> statuses) {
        data.setStatuses(statuses);
        return this;
    }

    public AysUserFilterBuilder withPhoneNumber(AysUserFilter.PhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AysUserFilterBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

}
