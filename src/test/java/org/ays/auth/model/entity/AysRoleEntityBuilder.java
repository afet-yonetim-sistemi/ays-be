package org.ays.auth.model.entity;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.util.Set;

public class AysRoleEntityBuilder extends TestDataBuilder<AysRoleEntity> {

    public AysRoleEntityBuilder() {
        super(AysRoleEntity.class);
    }

    public AysRoleEntityBuilder withValidValues() {
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

    public AysRoleEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysRoleEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleEntityBuilder withStatus(RoleStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysRoleEntityBuilder withPermissions(Set<AysPermissionEntity> permissionEntities) {
        data.setPermissions(permissionEntities);
        return this;
    }

    public AysRoleEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitution(null);
        return this;
    }

}
