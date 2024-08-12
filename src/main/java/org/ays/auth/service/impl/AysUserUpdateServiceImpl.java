package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.auth.util.exception.AysUserAlreadyDeletedException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.util.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.auth.util.exception.AysUserNotPassiveException;
import org.ays.common.model.AysPhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service implementation for updating users.
 * This service handles the update operation of existing users based on the provided update request.
 */
@Service
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
                .filter(userFromDatabase -> identity.getInstitutionId().equals(userFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (!(user.isActive() || user.isPassive())) {
            throw new AysUserIsNotActiveOrPassiveException(id);
        }

        final AysPhoneNumber phoneNumber = AysPhoneNumber.builder()
                .countryCode(updateRequest.getPhoneNumber().getCountryCode())
                .lineNumber(updateRequest.getPhoneNumber().getLineNumber())
                .build();

        this.validatePhoneNumber(user, phoneNumber);
        this.validateEmailAddress(user, updateRequest.getEmailAddress());
        this.validateRolesAndSet(user, updateRequest.getRoleIds());

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setEmailAddress(updateRequest.getEmailAddress());
        user.setCity(updateRequest.getCity());
        user.setPhoneNumber(phoneNumber);

        userSavePort.save(user);
    }


    /**
     * Activates a user by ID if the user is currently passive.
     * This method retrieves the user by the provided ID and activates the user
     *
     * @param id The unique identifier of the user to be activated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserNotPassiveException if the user is not in a passive state and cannot be activated.
     */
    @Override
    public void activate(String id) {

        final AysUser user = userReadPort.findById(id)
                .filter(userFromDatabase -> identity.getInstitutionId().equals(userFromDatabase.getInstitution().getId()))
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (!user.isPassive()) {
            throw new AysUserNotPassiveException(AysUserStatus.PASSIVE);
        }

        user.activate();
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
     * Checks the existence of roles by their IDs and returns the corresponding role entities.
     *
     * @param user    The user being updated.
     * @param roleIds The set of role IDs to be checked and retrieved.
     * @throws AysRolesNotExistException if any of the provided role IDs do not exist.
     */
    private void validateRolesAndSet(final AysUser user, final Set<String> roleIds) {

        boolean isRoleNotChanged = user.getRoles().stream()
                .allMatch(role -> roleIds.contains(role.getId()));
        if (isRoleNotChanged) {
            return;
        }

        final List<AysRole> roles = roleReadPort.findAllByIds(roleIds).stream()
                .filter(AysRole::isActive)
                .filter(role -> identity.getInstitutionId().equals(role.getInstitution().getId()))
                .toList();

        if (roles.size() == roleIds.size()) {
            user.setRoles(roles);
            return;
        }

        final List<String> notExistsRoleIds = roleIds.stream()
                .filter(roleId -> roles.stream()
                        .noneMatch(roleEntity -> roleEntity.getId().equals(roleId)))
                .toList();

        throw new AysRolesNotExistException(notExistsRoleIds);
    }

}
