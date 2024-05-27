package org.ays.user.service;

import org.ays.user.model.dto.request.RoleCreateRequest;

public interface RoleService {

    void createRole(RoleCreateRequest roleCreateRequest);
}
