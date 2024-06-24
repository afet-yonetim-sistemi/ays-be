package org.ays.auth.model.entity;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class RoleEntityBuilder extends TestDataBuilder<AysRoleEntity> {

    public RoleEntityBuilder() {
        super(AysRoleEntity.class);
    }

    public RoleEntityBuilder withValidValues() {
        Set<AysPermissionEntity> permissionEntities = Set.of(
                new AysPermissionEntityBuilder().withValidValues().build(),
                new AysPermissionEntityBuilder().withValidValues().withName("institution:page").withCategory(PermissionCategory.PAGE).build()
        );
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("admin")
                .withPermissions(permissionEntities)
                .withStatus(RoleStatus.ACTIVE);
    }

    public RoleEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public RoleEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public RoleEntityBuilder withStatus(RoleStatus status) {
        data.setStatus(status);
        return this;
    }

    public RoleEntityBuilder withPermissions(Set<AysPermissionEntity> permissionEntities) {
        data.setPermissions(permissionEntities);
        return this;
    }

    public RoleEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitution(null);
        return this;
    }

}
