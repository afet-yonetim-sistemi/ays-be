package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;
import java.util.UUID;

public class AysRoleUpdateRequestBuilder extends TestDataBuilder<AysRoleUpdateRequest> {

    public AysRoleUpdateRequestBuilder() {
        super(AysRoleUpdateRequest.class);
    }

    public AysRoleUpdateRequestBuilder withValidValues() {
        return this
                .withPermissionIds(Set.of(UUID.randomUUID().toString()));
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
