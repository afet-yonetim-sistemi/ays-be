package org.ays.user.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.enums.PermissionCategory;

public class PermissionEntityBuilder extends TestDataBuilder<PermissionEntity> {

    public PermissionEntityBuilder() {
        super(PermissionEntity.class);
    }

    public PermissionEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("user:list")
                .withCategory(PermissionCategory.USER_MANAGEMENT);
    }

    public PermissionEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public PermissionEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public PermissionEntityBuilder withCategory(PermissionCategory category) {
        data.setCategory(category);
        return this;
    }

}
