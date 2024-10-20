package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.exception.AysPermissionNotExistException;
import org.ays.auth.exception.AysRoleAlreadyExistsByNameException;
import org.ays.auth.exception.AysUserNotSuperAdminException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.service.AysRoleCreateService;
import org.ays.institution.model.Institution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Service implementation for creating roles in the system.
 * <p>
 * This service handles the creation of roles based on the provided create request. It verifies the uniqueness
 * of the role name, checks the existence of permissions, and saves the new role with the associated permissions.
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
class AysRoleCreateServiceImpl implements AysRoleCreateService {

    private final AysRoleReadPort roleReadPort;
    private final AysRoleSavePort roleSavePort;
    private final AysPermissionReadPort permissionReadPort;

    private final AysIdentity identity;

    /**
     * Creates a new role based on the provided create request.
     * <p>
     * This method validates the uniqueness of the role name, checks the existence of permissions,
     * and saves the new role with the associated permissions.
     * </p>
     *
     * @param createRequest the request object containing the role name and permission IDs
     * @throws AysRoleAlreadyExistsByNameException if a role with the same name already exists
     * @throws AysPermissionNotExistException      if any of the specified permission IDs do not exist
     * @throws AysUserNotSuperAdminException       if the current user is not authorized to assign super permissions
     */
    @Override
    public void create(final AysRoleCreateRequest createRequest) {

        this.checkExistingRoleName(createRequest.getName());

        final List<AysPermission> permissions = this.checkExistingPermissionsAndGet(createRequest.getPermissionIds());

        final AysRole role = AysRole.builder()
                .name(createRequest.getName())
                .institution(Institution.builder().id(identity.getInstitutionId()).build())
                .permissions(permissions)
                .status(AysRoleStatus.ACTIVE)
                .build();

        roleSavePort.save(role);
    }

    /**
     * Checks if a role with the specified name already exists.
     *
     * @param name the name of the role to check
     * @throws AysRoleAlreadyExistsByNameException if a role with the same name already exists
     */
    private void checkExistingRoleName(final String name) {
        roleReadPort.findByName(name).ifPresent(role -> {
            throw new AysRoleAlreadyExistsByNameException(name);
        });
    }

    /**
     * Checks the existence of permissions based on the provided permission IDs.
     * Verifies if all permission IDs exist and validates super admin restrictions.
     *
     * @param permissionIds the set of permission IDs to check
     * @return the list of permissions corresponding to the provided IDs
     * @throws AysPermissionNotExistException if any of the permission IDs do not exist
     * @throws AysUserNotSuperAdminException  if the current user is not authorized to assign super permissions
     */
    private List<AysPermission> checkExistingPermissionsAndGet(final Set<String> permissionIds) {
        final List<AysPermission> permissions = permissionReadPort.findAllByIds(permissionIds);

        if (permissions.size() != permissionIds.size()) {

            final List<String> notExistsPermissionIds = permissionIds.stream()
                    .filter(permissionId -> permissions.stream()
                            .noneMatch(permissionEntity -> permissionEntity.getId().equals(permissionId)))
                    .toList();

            throw new AysPermissionNotExistException(notExistsPermissionIds);
        }

        if (identity.isSuperAdmin()) {
            return permissions;
        }

        boolean haveSuperPermissions = permissions.stream().anyMatch(AysPermission::isSuper);
        if (haveSuperPermissions) {
            throw new AysUserNotSuperAdminException(identity.getUserId());
        }

        return permissions;
    }

}
