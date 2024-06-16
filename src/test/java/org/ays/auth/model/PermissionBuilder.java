package org.ays.auth.model;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

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
