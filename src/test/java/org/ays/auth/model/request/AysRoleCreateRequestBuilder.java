package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;
import java.util.UUID;

public class AysRoleCreateRequestBuilder extends TestDataBuilder<AysRoleCreateRequest> {

    public AysRoleCreateRequestBuilder() {
        super(AysRoleCreateRequest.class);
    }

    public AysRoleCreateRequestBuilder withValidValues() {
        return this
                .withPermissionIds(Set.of(UUID.randomUUID().toString()));
    }

    public AysRoleCreateRequestBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysRoleCreateRequestBuilder withPermissionIds(Set<String> permissionIds) {
        data.setPermissionIds(permissionIds);
        return this;
    }

}
