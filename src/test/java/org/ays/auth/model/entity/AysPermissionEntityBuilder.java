package org.ays.auth.model.entity;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

public class AysPermissionEntityBuilder extends TestDataBuilder<AysPermissionEntity> {

    public AysPermissionEntityBuilder() {
        super(AysPermissionEntity.class);
    }

    public AysPermissionEntityBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withName("user:list")
                .withCategory(PermissionCategory.USER_MANAGEMENT);
    }

    public AysPermissionEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysPermissionEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysPermissionEntityBuilder withCategory(PermissionCategory category) {
        data.setCategory(category);
        return this;
    }

    public AysPermissionEntityBuilder withIsSuper(boolean isSuper) {
        data.setSuper(isSuper);
        return this;
    }

}
