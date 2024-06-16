package org.ays.auth.model.dto.response;

import org.ays.auth.model.enums.PermissionCategory;
import org.ays.auth.model.response.PermissionsResponse;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

public class PermissionsResponseBuilder extends TestDataBuilder<PermissionsResponse> {

    public PermissionsResponseBuilder() {
        super(PermissionsResponse.class);
    }

    public PermissionsResponseBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withCategory(PermissionCategory.SUPER_ADMIN);
    }

    public PermissionsResponseBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public PermissionsResponseBuilder withCategory(PermissionCategory category) {
        data.setCategory(category);
        return this;
    }

}
