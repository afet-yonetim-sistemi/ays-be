package org.ays.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.dto.request.RoleCreateRequest;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.service.RoleService;
import org.ays.user.util.exception.AysPermissionNotExistException;
import org.ays.user.util.exception.AysRoleAlreadyExistsByNameException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

// TODO : Add Javadoc
@Service
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {

    private final AysIdentity identity;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // TODO : Add Javadoc
    @Override
    public void create(final RoleCreateRequest createRequest) {

        this.checkExistingRoleName(createRequest.getName());

        final Set<PermissionEntity> permissionEntities = this.checkExistingPermissionsAndGet(createRequest.getPermissionIds());

        final RoleEntity roleEntity = RoleEntity.builder()
                .name(createRequest.getName())
                .institutionId(identity.getInstitutionId())
                .permissions(permissionEntities)
                .build();

        roleRepository.save(roleEntity);
    }

    // TODO : Add Javadoc
    private void checkExistingRoleName(final String name) {
        roleRepository.findByName(name)
                .ifPresent(roleEntity -> {
                    throw new AysRoleAlreadyExistsByNameException(name);
                });
    }

    // TODO : Add Javadoc
    private Set<PermissionEntity> checkExistingPermissionsAndGet(final Set<String> permissionIds) {
        final Set<PermissionEntity> permissionEntities = permissionRepository.findAllByIdIn(permissionIds);

        if (permissionEntities.size() == permissionIds.size()) {
            return permissionEntities;
        }

        Set<String> notExistsPermissionIds = permissionIds.stream().filter(
                permissionId -> permissionEntities.stream()
                        .noneMatch(permissionEntity -> permissionEntity.getId().equals(permissionId))
        ).collect(Collectors.toSet());

        throw new AysPermissionNotExistException(notExistsPermissionIds);
    }

}
