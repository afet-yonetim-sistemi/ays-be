package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.exception.AysInvalidRoleStatusException;
import org.ays.auth.exception.AysPermissionNotExistException;
import org.ays.auth.exception.AysRoleAlreadyDeletedException;
import org.ays.auth.exception.AysRoleAlreadyExistsByNameException;
import org.ays.auth.exception.AysRoleAssignedToUserException;
import org.ays.auth.exception.AysRoleNotExistByIdException;
import org.ays.auth.exception.AysUserNotSuperAdminException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.request.AysRoleUpdateRequest;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.service.AysRoleUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for updating roles.
 * This service handles the updating of existing roles based on the provided update request,
 * ensuring permissions and role name uniqueness are validated before saving.
 */
@Service
@Transactional
@RequiredArgsConstructor
class AysRoleUpdateServiceImpl implements AysRoleUpdateService {

    private final AysRoleReadPort roleReadPort;
    private final AysRoleSavePort roleSavePort;
    private final AysPermissionReadPort permissionReadPort;

    private final AysIdentity identity;


    /**
     * Updates an existing role identified by its ID.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Ensures the role exists and belongs to the same institution as the current user.</li>
     *     <li>Validates the uniqueness of the role name within the institution.</li>
     *     <li>Checks if the provided permissions exist and validates the user's authorization for assigning super permissions.</li>
     *     <li>Updates the role's attributes such as name and permissions, and persists the changes.</li>
     * </ul>
     * </p>
     *
     * @param id            The ID of the role to update.
     * @param updateRequest The request object containing updated data for the role.
     * @throws AysRoleAlreadyExistsByNameException if a role with the same name already exists, excluding the current role ID.
     * @throws AysPermissionNotExistException      if any of the specified permission IDs do not exist.
     * @throws AysUserNotSuperAdminException       if the current user does not have super admin privileges required for assigning super permissions.
     * @throws AysRoleNotExistByIdException        if the role with the given ID does not exist or does not belong to the current user's institution.
     */
    @Override
    public void update(final String id,
                       final AysRoleUpdateRequest updateRequest) {

        final String institutionId = identity.getInstitutionId();
        final AysRole role = roleReadPort.findById(id)
                .filter(roleFromDatabase -> institutionId.equals(roleFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysRoleNotExistByIdException(id));

        final boolean isRoleNameChanged = !role.getName().equals(updateRequest.getName());
        if (isRoleNameChanged) {
            this.checkExistingRoleNameByWithoutId(id, updateRequest.getName(), institutionId);
        }

        final Set<String> existingPermissionIds = role.getPermissions().stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());
        final boolean permissionsChanged = !existingPermissionIds.equals(updateRequest.getPermissionIds());
        if (permissionsChanged) {
            this.validatePermissions(updateRequest.getPermissionIds());
        }

        role.update(
                updateRequest.getName(),
                updateRequest.getPermissionIds(),
                identity.getUserId()
        );
        roleSavePort.save(role);
    }

    /**
     * Checks the existence of another role with the same name, excluding the current role ID.
     * <p>
     * This method ensures that the role name is unique within the institution for all roles
     * except the one being updated.
     * </p>
     *
     * @param id            The ID of the role being updated.
     * @param name          The name to check for uniqueness.
     * @param institutionId The ID of the institution to which the role belongs.
     * @throws AysRoleAlreadyExistsByNameException if a role with the same name already exists, excluding the current role ID.
     */
    private void checkExistingRoleNameByWithoutId(final String id, final String name, final String institutionId) {
        roleReadPort.findByNameAndInstitutionId(name, institutionId)
                .filter(role -> !id.equals(role.getId()))
                .ifPresent(role -> {
                    throw new AysRoleAlreadyExistsByNameException(name);
                });
    }

    /**
     * Validates the specified permission IDs and checks user authorization.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Verifies if all provided permission IDs exist in the system.</li>
     *     <li>Ensures only super admin users can assign super permissions.</li>
     *     <li>Throws appropriate exceptions for missing permissions or unauthorized access attempts.</li>
     * </ul>
     * </p>
     *
     * @param permissionIds the set of permission IDs to validate.
     * @throws AysPermissionNotExistException if any of the specified permissions do not exist.
     * @throws AysUserNotSuperAdminException  if a non-super admin user attempts to assign super permissions.
     */
    private void validatePermissions(final Set<String> permissionIds) {

        final List<AysPermission> permissions = permissionReadPort.findAllByIds(permissionIds);

        if (permissions.size() != permissionIds.size()) {

            final List<String> notExistsPermissionIds = permissionIds.stream()
                    .filter(permissionId -> permissions.stream()
                            .noneMatch(permissionEntity -> permissionEntity.getId().equals(permissionId)))
                    .toList();

            throw new AysPermissionNotExistException(notExistsPermissionIds);
        }

        if (identity.isSuperAdmin()) {
            return;
        }

        boolean haveSuperPermissions = permissions.stream().anyMatch(AysPermission::isSuper);
        if (haveSuperPermissions) {
            throw new AysUserNotSuperAdminException(identity.getUserId());
        }
    }


    /**
     * Activates an existing role.
     * <p>
     * This method sets the status of the role identified by its ID to active. If the role does not exist,
     * an exception is thrown. Additionally, if the role's status is not {@link AysRoleStatus#PASSIVE},
     * an exception is thrown.
     * </p>
     *
     * @param id The ID of the role to activate.
     * @throws AysRoleNotExistByIdException  if a role with the given ID does not exist.
     * @throws AysInvalidRoleStatusException if the role's current status is not {@link AysRoleStatus#PASSIVE}.
     */
    @Override
    @SuppressWarnings("java:S1941")
    public void activate(String id) {

        final AysRole role = roleReadPort.findById(id)
                .filter(roleFromDatabase -> identity.getInstitutionId().equals(roleFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysRoleNotExistByIdException(id));

        if (!role.isPassive()) {
            throw new AysInvalidRoleStatusException(AysRoleStatus.PASSIVE);
        }

        role.activate();
        roleSavePort.save(role);
    }


    /**
     * Passivates an existing role.
     * <p>
     * This method sets the status of the role identified by its ID to passivate.
     * It also verifies that the role belongs to the same institution as the current user's institution
     * and no user is assigned to the role.
     * </p>
     *
     * @param id The ID of the role to passivate.
     * @throws AysRoleNotExistByIdException   if a role with the given ID does not exist.
     * @throws AysRoleAssignedToUserException if any user is assigned to the role.
     * @throws AysInvalidRoleStatusException  if the role's current status is not {@link AysRoleStatus#ACTIVE}.
     */
    @Override
    @SuppressWarnings("java:S1941")
    public void passivate(String id) {

        final AysRole role = roleReadPort.findById(id)
                .filter(roleFromDatabase -> identity.getInstitutionId().equals(roleFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysRoleNotExistByIdException(id));

        if (roleReadPort.isRoleUsing(id)) {
            throw new AysRoleAssignedToUserException(id);
        }

        if (!role.isActive()) {
            throw new AysInvalidRoleStatusException(AysRoleStatus.ACTIVE);
        }

        role.passivate();
        roleSavePort.save(role);
    }


    /**
     * Deletes an existing role identified by its ID.
     * It also verifies that the role belongs to the same institution as the current user's institution.
     *
     * @param id The ID of the role to delete.
     * @throws AysRoleNotExistByIdException   if no role exists with the provided ID.
     * @throws AysRoleAssignedToUserException if users are assigned to the role.
     * @throws AysRoleAlreadyDeletedException if the role is already marked as deleted.
     */
    @Override
    @SuppressWarnings("java:S1941")
    public void delete(final String id) {

        final AysRole role = roleReadPort.findById(id)
                .filter(roleFromDatabase -> identity.getInstitutionId().equals(roleFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysRoleNotExistByIdException(id));

        if (roleReadPort.isRoleUsing(id)) {
            throw new AysRoleAssignedToUserException(id);
        }

        if (role.isDeleted()) {
            throw new AysRoleAlreadyDeletedException(id);
        }

        role.delete();
        roleSavePort.save(role);
    }

}
