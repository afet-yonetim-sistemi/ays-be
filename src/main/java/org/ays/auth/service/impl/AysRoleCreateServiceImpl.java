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
     * This method performs the following steps:
     * <ul>
     *     <li>Validates the uniqueness of the role name within the institution.</li>
     *     <li>Checks if the provided permission IDs exist.</li>
     *     <li>Ensures the user has the appropriate authority to assign permissions, especially super permissions.</li>
     *     <li>Saves the new role with its associated permissions.</li>
     * </ul>
     * </p>
     *
     * @param createRequest The request object containing the role name and permission IDs.
     * @throws AysRoleAlreadyExistsByNameException If a role with the same name already exists in the institution.
     * @throws AysPermissionNotExistException      If any of the specified permission IDs do not exist.
     * @throws AysUserNotSuperAdminException       If the current user is not authorized to assign super permissions.
     */
    @Override
    public void create(final AysRoleCreateRequest createRequest) {

        final String institutionId = identity.getInstitutionId();
        this.checkExistingRoleName(createRequest.getName(), institutionId);

        final List<AysPermission> permissions = this.checkExistingPermissionsAndGet(createRequest.getPermissionIds());

        final AysRole role = AysRole.builder()
                .name(createRequest.getName())
                .institution(Institution.builder().id(institutionId).build())
                .permissions(permissions)
                .status(AysRoleStatus.ACTIVE)
                .build();

        roleSavePort.save(role);
    }

    /**
     * Checks if a role with the specified name already exists within the institution.
     *
     * @param name          The name of the role to check.
     * @param institutionId The ID of the institution where the role is being created.
     * @throws AysRoleAlreadyExistsByNameException If a role with the same name already exists in the institution.
     */
    private void checkExistingRoleName(final String name, final String institutionId) {
        roleReadPort.findByNameAndInstitutionId(name, institutionId)
                .ifPresent(role -> {
                    throw new AysRoleAlreadyExistsByNameException(name);
                });
    }

    /**
     * Checks the existence of permissions based on the provided permission IDs
     * and ensures that the user has the authority to assign the permissions.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Retrieves all permissions corresponding to the provided IDs.</li>
     *     <li>Validates that all requested permissions exist in the system.</li>
     *     <li>Ensures that super permissions can only be assigned by super admins.</li>
     * </ul>
     * </p>
     *
     * @param permissionIds The set of permission IDs to check.
     * @return A list of {@link AysPermission} objects corresponding to the provided IDs.
     * @throws AysPermissionNotExistException If any of the permission IDs do not exist.
     * @throws AysUserNotSuperAdminException  If the current user is not authorized to assign super permissions.
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
