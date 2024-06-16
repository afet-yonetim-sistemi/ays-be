package org.ays.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.request.RoleCreateRequest;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.service.RoleCreateService;
import org.ays.user.util.exception.AysPermissionNotExistException;
import org.ays.user.util.exception.AysRoleAlreadyExistsByNameException;
import org.ays.user.util.exception.AysUserNotSuperAdminException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for creating roles.
 * <p>
 * The {@link RoleCreateServiceImpl} class provides the implementation for the {@link RoleCreateService}
 * interface, handling operations related to role creation. It interacts with the {@link RoleRepository}
 * and {@link PermissionRepository} to manage role and permission data.
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
class RoleCreateServiceImpl implements RoleCreateService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final AysIdentity identity;

    /**
     * Creates a new role based on the provided request.
     * <p>
     * This method checks if a role with the same name already exists and verifies the permissions
     * provided in the request. If all checks pass, it saves the new role entity.
     * </p>
     *
     * @param createRequest the request object containing the details for the new role.
     */
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

    /**
     * Checks if a role with the specified name already exists.
     * <p>
     * If a role with the given name is found, an {@link AysRoleAlreadyExistsByNameException} is thrown.
     * </p>
     *
     * @param name the name of the role to check.
     */
    private void checkExistingRoleName(final String name) {
        roleRepository.findByName(name)
                .ifPresent(roleEntity -> {
                    throw new AysRoleAlreadyExistsByNameException(name);
                });
    }

    /**
     * Checks if the provided permission IDs exist and retrieves the corresponding permission entities.
     * <p>
     * If any of the provided permission IDs do not exist, an {@link AysPermissionNotExistException} is thrown.
     * Additionally, if the user is not a super admin and any of the permissions are super permissions,
     * an {@link AysUserNotSuperAdminException} is thrown.
     * </p>
     *
     * @param permissionIds the set of permission IDs to check and retrieve.
     * @return a set of {@link PermissionEntity} objects corresponding to the provided IDs.
     */
    private Set<PermissionEntity> checkExistingPermissionsAndGet(final Set<String> permissionIds) {
        final Set<PermissionEntity> permissionEntitiesFromDatabase = permissionRepository.findAllByIdIn(permissionIds);

        if (permissionEntitiesFromDatabase.size() == permissionIds.size()) {

            if (identity.isSuperAdmin()) {
                return permissionEntitiesFromDatabase;
            }

            boolean haveSuperPermissions = permissionEntitiesFromDatabase.stream()
                    .anyMatch(PermissionEntity::isSuper);
            if (haveSuperPermissions) {
                throw new AysUserNotSuperAdminException(identity.getUserId());
            }

            return permissionEntitiesFromDatabase;
        }

        Set<String> notExistsPermissionIds = permissionIds.stream()
                .filter(permissionId -> permissionEntitiesFromDatabase.stream()
                        .noneMatch(permissionEntity -> permissionEntity.getId().equals(permissionId)))
                .collect(Collectors.toSet());

        throw new AysPermissionNotExistException(notExistsPermissionIds);
    }

}
