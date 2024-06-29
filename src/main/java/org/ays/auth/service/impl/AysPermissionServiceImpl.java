package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.service.AysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing permissions.
 * Retrieves permissions based on the user's identity.
 */
@Service
@RequiredArgsConstructor
class AysPermissionServiceImpl implements AysPermissionService {

    private final AysPermissionReadPort permissionReadPort;

    private final AysIdentity identity;


    /**
     * Retrieves all permissions based on the user's identity.
     * If the user is a super admin, all permissions are fetched.
     * Otherwise, only non-super permissions are retrieved.
     *
     * @return A list of {@link AysPermission} objects based on the user's identity.
     */
    @Override
    public List<AysPermission> findAll() {

        if (identity.isSuperAdmin()) {
            return permissionReadPort.findAll();
        }

        return permissionReadPort.findAllByIsSuperFalse();
    }

}
