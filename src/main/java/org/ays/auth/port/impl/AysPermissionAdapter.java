package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.entity.AysPermissionEntity;
import org.ays.auth.model.mapper.AysPermissionEntityToDomainMapper;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.repository.AysPermissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Adapter class implementing the {@link AysPermissionReadPort} interface.
 * Retrieves {@link AysPermission} from the repository and maps them to domain models.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AysPermissionAdapter implements AysPermissionReadPort {

    private final AysPermissionRepository permissionRepository;


    private final AysPermissionEntityToDomainMapper permissionEntityToDomainMapper = AysPermissionEntityToDomainMapper.initialize();


    /**
     * Retrieves all {@link AysPermission} from the repository.
     *
     * @return A set of all {@link AysPermission}.
     */
    @Override
    public List<AysPermission> findAll() {
        List<AysPermissionEntity> permissionEntities = permissionRepository.findAll();
        return permissionEntityToDomainMapper.map(permissionEntities);
    }

    /**
     * Retrieves all {@link AysPermission} where {@code isSuper} flag is false from the repository.
     *
     * @return A set of {@link AysPermission} where {@code isSuper} is false.
     */
    @Override
    public List<AysPermission> findAllByIsSuperFalse() {
        List<AysPermissionEntity> permissionEntities = permissionRepository.findAllByIsSuperFalse();
        return permissionEntityToDomainMapper.map(permissionEntities);
    }

    /**
     * Retrieves {@link AysPermission} with IDs present in the given set from the repository.
     *
     * @param permissionIds The set of permission IDs to retrieve.
     * @return A set of {@link AysPermission} matching the provided IDs.
     */
    @Override
    public List<AysPermission> findAllByIdIn(final Set<String> permissionIds) {
        List<AysPermissionEntity> permissionEntities = permissionRepository.findAllByIdIn(permissionIds);
        return permissionEntityToDomainMapper.map(permissionEntities);
    }

}
