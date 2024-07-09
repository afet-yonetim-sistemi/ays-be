package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.auth.util.exception.AysInvalidUserStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Service implementation for updating users.
 * This service handles the update operation of existing users based on the provided update request.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AysUserUpdateServiceImpl implements AysUserUpdateService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;
    private final AysRoleReadPort roleReadPort;


    /**
     * Updates an existing user with the given ID based on the provided update request.
     *
     * @param id            The unique identifier of the user to be updated.
     * @param updateRequest The request object containing the updated user information.
     * @throws AysUserNotExistByIdException  if a user with the given ID does not exist.
     * @throws AysInvalidUserStatusException if the user's status is not valid for an update.
     */
    @Override
    public void update(final String id,
                       final AysUserUpdateRequest updateRequest) {

        final AysUser user = userReadPort.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (!user.isStatusValid()) {
            throw new AysInvalidUserStatusException(id);

        }

        final List<AysRole> roles = this.checkExistingRolesAndGet(updateRequest.getRoleIds());

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setEmailAddress(updateRequest.getEmailAddress());
        user.setCity(updateRequest.getCity());
        user.setPhoneNumber(updateRequest.getPhoneNumber());
        user.setRoles(roles);

        userSavePort.save(user);
    }


    /**
     * Checks the existence of roles by their IDs and returns the corresponding role entities.
     *
     * @param roleIds The set of role IDs to be checked and retrieved.
     * @return A list of {@link AysRole} objects corresponding to the provided role IDs.
     * @throws AysRolesNotExistException if any of the provided role IDs do not exist.
     */
    private List<AysRole> checkExistingRolesAndGet(final Set<String> roleIds) {
        final List<AysRole> roles = roleReadPort.findAllByIds(roleIds);

        if (roles.size() != roleIds.size()) {

            final List<String> notExistsRoleIds = roleIds.stream()
                    .filter(roleId -> roles.stream()
                            .noneMatch(roleEntity -> roleEntity.getId().equals(roleId)))
                    .toList();

            throw new AysRolesNotExistException(notExistsRoleIds);
        }

        return roles;
    }

}
