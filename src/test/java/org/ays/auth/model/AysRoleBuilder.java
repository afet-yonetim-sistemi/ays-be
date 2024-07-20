package org.ays.auth.model;

import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;

import java.util.List;

public class AysRoleBuilder extends TestDataBuilder<AysRole> {

    public AysRoleBuilder() {
        super(AysRole.class);
    }

    public AysRoleBuilder withValidValues() {
        List<AysPermission> permissions = List.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().withName("institution:page").withCategory(AysPermissionCategory.PAGE).build()
        );
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("admin")
                .withPermissions(permissions)
                .withStatus(AysRoleStatus.ACTIVE)
                .withInstitution(new InstitutionBuilder().withValidValues().build());
    }

    public AysRoleBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysRoleBuilder withoutId() {
        data.setId(null);
        return this;
    }

    public AysRoleBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleBuilder withStatus(AysRoleStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysRoleBuilder withPermissions(List<AysPermission> permissions) {
        data.setPermissions(permissions);
        return this;
    }

    public AysRoleBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

}
