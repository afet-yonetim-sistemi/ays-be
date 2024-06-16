package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.Permission;
import org.ays.auth.model.entity.PermissionEntity;
import org.ays.auth.model.mapper.PermissionEntityToPermissionMapper;
import org.ays.auth.repository.PermissionRepository;
import org.ays.auth.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service implementation for managing permissions.
 * <p>
 * The {@link PermissionServiceImpl} class provides the implementation for the {@link PermissionService}
 * interface, handling operations related to permissions. It interacts with the {@link PermissionRepository}
 * to retrieve permission data and uses a mapper to convert entities to domain models.
 * </p>
 */
@Service
@RequiredArgsConstructor
class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final AysIdentity identity;

    private final PermissionEntityToPermissionMapper permissionEntityToPermissionMapper = PermissionEntityToPermissionMapper.initialize();

    /**
     * Retrieves all permissions available in the system.
     * <p>
     * If the current user is a super admin, all permissions are returned.
     * Otherwise, only non-super permissions are returned.
     * </p>
     *
     * @return a list of {@link Permission} objects representing the permissions.
     */
    @Override
    public List<Permission> findAll() {

        if (identity.isSuperAdmin()) {
            final List<PermissionEntity> permissionEntities = permissionRepository.findAll();
            return permissionEntityToPermissionMapper.map(permissionEntities);
        }

        Set<PermissionEntity> permissionEntities = permissionRepository.findAllByIsSuperFalse();
        return permissionEntityToPermissionMapper.map(permissionEntities);
    }

}
