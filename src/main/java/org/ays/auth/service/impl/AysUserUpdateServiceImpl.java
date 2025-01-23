package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.exception.AysRolesNotExistException;
import org.ays.auth.exception.AysUserAlreadyActiveException;
import org.ays.auth.exception.AysUserAlreadyDeletedException;
import org.ays.auth.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.exception.AysUserAlreadyPassiveException;
import org.ays.auth.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.exception.AysUserNotExistByIdException;
import org.ays.auth.exception.AysUserNotPassiveException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.common.model.AysPhoneNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for updating users.
 * This service handles the update operation of existing users based on the provided update request.
 */
@Service
@Transactional
@RequiredArgsConstructor
class AysUserUpdateServiceImpl implements AysUserUpdateService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;
    private final AysRoleReadPort roleReadPort;

    private final AysIdentity identity;


    /**
     * Updates an existing user with the given ID based on the provided update request.
     *
     * @param id            The unique identifier of the user to be updated.
     * @param updateRequest The request object containing the updated user information.
     * @throws AysUserNotExistByIdException         if a user with the given ID does not exist.
     * @throws AysUserIsNotActiveOrPassiveException if the user's status is not valid for an update.
     */
    @Override
    public void update(final String id,
                       final AysUserUpdateRequest updateRequest) {

        final AysUser user = userReadPort.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        final String institutionId = identity.getInstitutionId();
        if (!institutionId.equals(user.getInstitution().getId())) {
            throw new AysUserNotExistByIdException(id);
        }

        if (!(user.isActive() || user.isPassive())) {
            throw new AysUserIsNotActiveOrPassiveException(id);
        }

        final AysPhoneNumber phoneNumber = AysPhoneNumber.builder()
                .countryCode(updateRequest.getPhoneNumber().getCountryCode())
                .lineNumber(updateRequest.getPhoneNumber().getLineNumber())
                .build();

        final boolean isPhoneNumberChanged = !user.getPhoneNumber().equals(phoneNumber);
        if (isPhoneNumberChanged) {
            this.validatePhoneNumber(user, phoneNumber);
        }

        final boolean isEmailChanged = !user.getEmailAddress().equals(updateRequest.getEmailAddress());
        if (isEmailChanged) {
            this.validateEmailAddress(user, updateRequest.getEmailAddress());
        }

        final Set<String> existingRoleIds = user.getRoles().stream()
                .map(AysRole::getId)
                .collect(Collectors.toSet());
        final boolean isRoleChanged = !existingRoleIds.equals(updateRequest.getRoleIds());
        if (isRoleChanged) {
            this.validateRoles(updateRequest.getRoleIds(), institutionId);
        }

        user.update(
                updateRequest.getEmailAddress(),
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                phoneNumber,
                updateRequest.getCity(),
                updateRequest.getRoleIds(),
                identity.getUserId()
        );
        userSavePort.save(user);
    }


    /**
     * Activates a user by ID if the user is currently passive.
     * This method retrieves the user by the provided ID and activates the user
     *
     * @param id The unique identifier of the user to be activated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserAlreadyActiveException if a user is already in an active state and cannot be activated.
     * @throws AysUserNotPassiveException   if the user is not in a passive state and cannot be activated.
     */
    @Override
    public void activate(String id) {

        final AysUser user = userReadPort.findById(id)
                .filter(userFromDatabase -> identity.getInstitutionId().equals(userFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (user.isActive()) {
            throw new AysUserAlreadyActiveException();
        }

        if (!user.isPassive()) {
            throw new AysUserNotPassiveException();
        }

        user.activate();
        userSavePort.save(user);
    }

    /**
     * Passivates (deactivates) a user by ID if the user is currently active.
     * This method retrieves the user by the provided ID and deactivates the user.
     *
     * @param id The unique identifier of the user to be passivated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserAlreadyPassiveException if the user is already in a passive state.
     * @throws AysUserNotActiveException if the user is not in an active state.
     */
    @Override
    public void passivate(String id) {

        final AysUser user = userReadPort.findById(id)
                .filter(userFromDatabase -> identity.getInstitutionId().equals(userFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (user.isPassive()) {
            throw new AysUserAlreadyPassiveException();
        }

        if (!user.isActive()) {
            throw new AysUserNotActiveException(id);
        }

        user.passivate();
        userSavePort.save(user);
    }


    /**
     * Deletes a user account by its ID.
     * <p>
     * This method retrieves a user by the given ID and ensures the user's institution ID
     * matches the identity's institution ID. If the user is already marked as deleted,
     * an {@link AysUserAlreadyDeletedException} is thrown. Otherwise, the user's status
     * is set to deleted and the changes are saved.
     * </p>
     *
     * @param id The ID of the user to delete.
     * @throws AysUserNotExistByIdException   If no user with the given ID exists or if the user does not belong to the caller's institution.
     * @throws AysUserAlreadyDeletedException If the user is already marked as deleted.
     */
    @Override
    public void delete(final String id) {

        final AysUser user = userReadPort.findById(id)
                .filter(userFromDatabase -> identity.getInstitutionId().equals(userFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (user.isDeleted()) {
            throw new AysUserAlreadyDeletedException(id);
        }

        user.delete();
        userSavePort.save(user);
    }


    /**
     * Validates the uniqueness of the provided phone number.
     * Checks if there is any existing user with the same phone number.
     *
     * @param user        The user being updated.
     * @param phoneNumber The phone number to be validated.
     * @throws AysUserAlreadyExistsByPhoneNumberException if the phone number is already associated with another user.
     */
    private void validatePhoneNumber(AysUser user, AysPhoneNumber phoneNumber) {

        if (user.getPhoneNumber().equals(phoneNumber)) {
            return;
        }

        userReadPort.findByPhoneNumber(phoneNumber)
                .filter(existingUser -> !existingUser.getId().equals(user.getId()))
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
                });
    }


    /**
     * Validates the uniqueness of the provided email address.
     * Checks if there is any existing user with the same email address.
     *
     * @param user         The user being updated.
     * @param emailAddress The email address to be validated.
     * @throws AysUserAlreadyExistsByEmailAddressException if the email address is already associated with another user.
     */
    private void validateEmailAddress(AysUser user, String emailAddress) {

        if (user.getEmailAddress().equals(emailAddress)) {
            return;
        }

        userReadPort.findByEmailAddress(emailAddress)
                .filter(existingUser -> !existingUser.getId().equals(user.getId()))
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByEmailAddressException(emailAddress);
                });
    }


    /**
     * Validates if all specified roles exist and are active within the given institution.
     * <p>
     * This method checks if each role ID corresponds to an active role in the specified institution.
     * If any role is not found or not active, an exception is thrown with the list of invalid role IDs.
     * </p>
     *
     * @param roleIds       the set of role IDs to validate
     * @param institutionId the ID of the institution where the roles should exist
     * @throws AysRolesNotExistException if any of the specified roles do not exist or are not valid for the institution
     */
    private void validateRoles(final Set<String> roleIds, final String institutionId) {

        final List<AysRole> roles = roleReadPort.findAllByIds(roleIds).stream()
                .filter(AysRole::isActive)
                .filter(role -> institutionId.equals(role.getInstitution().getId()))
                .toList();

        if (roles.size() != roleIds.size()) {

            final List<String> notExistsRoleIds = roleIds.stream()
                    .filter(roleId -> roles.stream()
                            .noneMatch(roleEntity -> roleEntity.getId().equals(roleId)))
                    .toList();

            throw new AysRolesNotExistException(notExistsRoleIds);
        }

    }

}
