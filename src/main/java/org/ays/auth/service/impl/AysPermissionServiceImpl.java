package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.service.AysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
     * Retrieves a filtered list of permissions based on the user's identity.
     * <p>
     * If the user has super admin privileges, all permissions are fetched and filtered to include only those
     * with the name "landing:page". Otherwise, only non-super permissions are retrieved and similarly filtered
     * by the "landing:page" name.
     * </p>
     *
     * @return a list of {@link AysPermission} objects matching the filter criteria based on the user's identity
     */
    @Override
    public List<AysPermission> findAll() {

        if (identity.isSuperAdmin()) {
            return permissionReadPort.findAll().stream()
                    .filter(permission -> !"landing:page".equals(permission.getName()))
                    .collect(Collectors.toList());
        }

        return permissionReadPort.findAllByIsSuperFalse().stream()
                .filter(permission -> !"landing:page".equals(permission.getName()))
                .collect(Collectors.toList());
    }

}
