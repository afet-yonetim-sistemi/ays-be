package org.ays.auth.model.entity;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

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

    public PermissionEntityBuilder withIsSuper(boolean isSuper) {
        data.setSuper(isSuper);
        return this;
    }

}
