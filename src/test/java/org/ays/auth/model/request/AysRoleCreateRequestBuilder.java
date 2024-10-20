package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

import java.util.Set;

public class AysRoleCreateRequestBuilder extends TestDataBuilder<AysRoleCreateRequest> {

    public AysRoleCreateRequestBuilder() {
        super(AysRoleCreateRequest.class);
    }

    public AysRoleCreateRequestBuilder withValidValues() {
        return this
                .withPermissionIds(Set.of("c85ae33d-4b77-4fb9-a2cc-440b4729f5d1"));
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
