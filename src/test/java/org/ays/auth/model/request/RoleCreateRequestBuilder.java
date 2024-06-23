package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;
import java.util.UUID;

public class RoleCreateRequestBuilder extends TestDataBuilder<AysRoleCreateRequest> {

    public RoleCreateRequestBuilder() {
        super(AysRoleCreateRequest.class);
    }

    public RoleCreateRequestBuilder withValidValues() {
        return this
                .withPermissionIds(Set.of(UUID.randomUUID().toString()));
    }

    public RoleCreateRequestBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public RoleCreateRequestBuilder withPermissionIds(Set<String> permissionIds) {
        data.setPermissionIds(permissionIds);
        return this;
    }

}
