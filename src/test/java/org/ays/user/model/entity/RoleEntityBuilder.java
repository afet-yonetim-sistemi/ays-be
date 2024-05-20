package org.ays.user.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.enums.PermissionCategory;
import org.ays.user.model.enums.RoleStatus;

import java.util.Set;

public class RoleEntityBuilder extends TestDataBuilder<RoleEntity> {

    public RoleEntityBuilder() {
        super(RoleEntity.class);
    }

    public RoleEntityBuilder withValidFields() {
        Set<PermissionEntity> permissionEntities = Set.of(
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().withName("institution:page").withCategory(PermissionCategory.PAGE).build()
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

    public RoleEntityBuilder withPermissions(Set<PermissionEntity> permissionEntities) {
        data.setPermissions(permissionEntities);
        return this;
    }

    public RoleEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitution(null);
        return this;
    }

}
