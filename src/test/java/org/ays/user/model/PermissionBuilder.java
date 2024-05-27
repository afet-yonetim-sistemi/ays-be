package org.ays.user.model;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.enums.PermissionCategory;

public class PermissionBuilder extends TestDataBuilder<Permission> {

    public PermissionBuilder() {
        super(Permission.class);
    }

    public PermissionBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withCategory(PermissionCategory.SUPER_ADMIN);
    }

    public PermissionBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public PermissionBuilder withCategory(PermissionCategory category) {
        data.setCategory(category);
        return this;
    }

}
