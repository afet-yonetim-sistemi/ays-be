package org.ays.auth.model;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;

import java.util.Set;

public class AysRoleBuilder extends TestDataBuilder<AysRole> {

    public AysRoleBuilder() {
        super(AysRole.class);
    }

    public AysRoleBuilder withValidValues() {
        Set<AysPermission> permissions = Set.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().withName("institution:page").withCategory(PermissionCategory.PAGE).build()
        );
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("admin")
                .withPermissions(permissions)
                .withStatus(RoleStatus.ACTIVE);
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

    public AysRoleBuilder withStatus(RoleStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysRoleBuilder withPermissions(Set<AysPermission> permissions) {
        data.setPermissions(permissions);
        return this;
    }

    public AysRoleBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

}
