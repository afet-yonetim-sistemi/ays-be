package org.ays.auth.model.entity;

import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;

import java.util.List;

public class AysRoleEntityBuilder extends TestDataBuilder<AysRoleEntity> {

    public AysRoleEntityBuilder() {
        super(AysRoleEntity.class);
    }

    public AysRoleEntityBuilder withValidValues() {
        List<AysPermissionEntity> permissionEntities = List.of(
                new AysPermissionEntityBuilder().withValidValues().build(),
                new AysPermissionEntityBuilder().withValidValues().withName("institution:page").withCategory(AysPermissionCategory.PAGE).build()
        );
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("admin")
                .withPermissions(permissionEntities)
                .withStatus(AysRoleStatus.ACTIVE);
    }

    public AysRoleEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysRoleEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleEntityBuilder withStatus(AysRoleStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysRoleEntityBuilder withPermissions(List<AysPermissionEntity> permissionEntities) {
        data.setPermissions(permissionEntities);
        return this;
    }

    public AysRoleEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

}
