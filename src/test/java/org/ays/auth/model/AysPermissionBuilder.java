package org.ays.auth.model;

import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

public class AysPermissionBuilder extends TestDataBuilder<AysPermission> {

    public AysPermissionBuilder() {
        super(AysPermission.class);
    }

    public AysPermissionBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withCategory(AysPermissionCategory.SUPER_ADMIN);
    }

    public AysPermissionBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysPermissionBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysPermissionBuilder withCategory(AysPermissionCategory category) {
        data.setCategory(category);
        return this;
    }

    public AysPermissionBuilder withIsSuper(boolean isSuper) {
        data.setSuper(isSuper);
        return this;
    }

}
