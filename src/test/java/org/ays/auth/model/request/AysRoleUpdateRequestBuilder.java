package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;

public class AysRoleUpdateRequestBuilder extends TestDataBuilder<AysRoleUpdateRequest> {

    public AysRoleUpdateRequestBuilder() {
        super(AysRoleUpdateRequest.class);
    }

    public AysRoleUpdateRequestBuilder withValidValues() {
        return this
                .withPermissionIds(Set.of("e27eaa10-837c-45a9-9bde-2d0912db95fb"));
    }

    public AysRoleUpdateRequestBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleUpdateRequestBuilder withPermissionIds(Set<String> permissionIds) {
        data.setPermissionIds(permissionIds);
        return this;
    }

}
