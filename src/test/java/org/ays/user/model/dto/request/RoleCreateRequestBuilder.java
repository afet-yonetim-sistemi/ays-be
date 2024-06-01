package org.ays.user.model.dto.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;
import java.util.UUID;

public class RoleCreateRequestBuilder extends TestDataBuilder<RoleCreateRequest> {

    public RoleCreateRequestBuilder() {
        super(RoleCreateRequest.class);
    }

    public RoleCreateRequestBuilder withValidFields() {
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
