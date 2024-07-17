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
import org.ays.auth.util.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.common.model.AysPhoneNumber;
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
class AysUserUpdateServiceImpl implements AysUserUpdateService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;
    private final AysRoleReadPort roleReadPort;


    /**
     * Updates an existing user with the given ID based on the provided update request.
     *
     * @param id The unique identifier of the user to be updated.
     * @param updateRequest The request object containing the updated user information.
     * @throws AysUserNotExistByIdException  if a user with the given ID does not exist.
     * @throws AysUserIsNotActiveOrPassiveException if the user's status is not valid for an update.
     */
    @Override
    public void update(final String id,
                       final AysUserUpdateRequest updateRequest) {

        final AysUser user = userReadPort.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (!(user.isActive() || user.isPassive())) {
            throw new AysUserIsNotActiveOrPassiveException(id);
        }

        final AysPhoneNumber phoneNumber = AysPhoneNumber.builder()
                .countryCode(updateRequest.getPhoneNumber().getCountryCode())
                .lineNumber(updateRequest.getPhoneNumber().getLineNumber())
                .build();

        if (user.getPhoneNumber() != phoneNumber) {
            this.validatePhoneNumber(id, phoneNumber);
        }

        if (!(user.getEmailAddress().equals(updateRequest.getEmailAddress()))) {
            this.validateEmailAddress(id, updateRequest.getEmailAddress());
        }

        final List<AysRole> roles = this.checkExistingRolesAndGet(updateRequest.getRoleIds());

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setEmailAddress(updateRequest.getEmailAddress());
        user.setCity(updateRequest.getCity());
        user.setPhoneNumber(phoneNumber);
        user.setRoles(roles);

        userSavePort.save(user);
    }


    /**
     * Validates the uniqueness of the provided phone number.
     * Checks if there is any existing user with the same phone number.
     *
     * @param id          The unique identifier of the user being updated.
     * @param phoneNumber The phone number to be validated.
     * @throws AysUserAlreadyExistsByPhoneNumberException if the phone number is already associated with another user.
     */
    private void validatePhoneNumber(String id, AysPhoneNumber phoneNumber) {
        userReadPort.findByPhoneNumber(phoneNumber)
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
                });
    }


    /**
     * Validates the uniqueness of the provided email address.
     * Checks if there is any existing user with the same email address.
     *
     * @param id           The unique identifier of the user being updated.
     * @param emailAddress The email address to be validated.
     * @throws AysUserAlreadyExistsByEmailException if the email address is already associated with another user.
     */
    private void validateEmailAddress(String id, String emailAddress) {
        userReadPort.findByEmailAddress(emailAddress)
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByEmailException(emailAddress);
                });
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

        if (roles.size() == roleIds.size()) {
            return roles;
        }

        final List<String> notExistsRoleIds = roleIds.stream()
                .filter(roleId -> roles.stream()
                        .noneMatch(roleEntity -> roleEntity.getId().equals(roleId)))
                .toList();

        throw new AysRolesNotExistException(notExistsRoleIds);
    }

}
