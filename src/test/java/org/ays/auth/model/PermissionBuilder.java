package org.ays.auth.model;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

public class PermissionBuilder extends TestDataBuilder<AysPermission> {

    public PermissionBuilder() {
        super(AysPermission.class);
    }

    public PermissionBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withCategory(PermissionCategory.SUPER_ADMIN);
    }

    public PermissionBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public PermissionBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public PermissionBuilder withCategory(PermissionCategory category) {
        data.setCategory(category);
        return this;
    }

    public PermissionBuilder withIsSuper(boolean isSuper) {
        data.setSuper(isSuper);
        return this;
    }

}
