package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserCreateRequestToDomainMapper;
import org.ays.auth.model.request.AysUserCreateRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserCreateService;
import org.ays.auth.service.AysUserMailService;
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.model.AysPhoneNumber;
import org.ays.institution.model.Institution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service implementation for creating new users in the system.
 * <p>
 * This class is responsible for handling the creation of new users,
 * including validation of input data, ensuring uniqueness of email addresses and phone numbers,
 * and setting roles. It ensures that the new user is properly activated and associated with
 * the correct institution. Additionally, it sends a password creation email to the new user
 * after successful creation.
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
class AysUserCreateServiceImpl implements AysUserCreateService {

    private final AysUserSavePort userSavePort;
    private final AysUserReadPort userReadPort;
    private final AysRoleReadPort roleReadPort;
    private final AysUserMailService userMailService;

    private final AysIdentity identity;


    private final AysUserCreateRequestToDomainMapper userCreateRequestToDomainMapper = AysUserCreateRequestToDomainMapper.initialize();


    /**
     * Creates a new user based on the provided request data.
     * <p>
     * Validates the email address and phone number for uniqueness, checks the existence of roles,
     * and assigns the roles to the user. The user is then activated and associated with the correct
     * institution. After successful creation, a password creation email is sent to the user.
     * </p>
     *
     * @param createRequest The request object containing data for the new user.
     * @throws AysUserAlreadyExistsByEmailException       if the email address is already associated with another user.
     * @throws AysUserAlreadyExistsByPhoneNumberException if the phone number is already associated with another user.
     * @throws AysRolesNotExistException                  if any of the provided role IDs do not exist.
     */
    @Override
    public void create(final AysUserCreateRequest createRequest) {

        this.validateEmailAddress(createRequest.getEmailAddress());

        final AysPhoneNumber phoneNumber = AysPhoneNumber.builder()
                .countryCode(createRequest.getPhoneNumber().getCountryCode())
                .lineNumber(createRequest.getPhoneNumber().getLineNumber())
                .build();
        this.validatePhoneNumber(phoneNumber);

        final AysUser user = userCreateRequestToDomainMapper.map(createRequest);

        this.validateRolesAndSet(user, createRequest.getRoleIds());

        user.activate();
        user.setInstitution(Institution.builder().id(identity.getInstitutionId()).build());
        user.setPassword(AysUser.Password.builder().value(UUID.randomUUID().toString()).build());

        AysUser savedUser = userSavePort.save(user);

        userMailService.sendPasswordCreateEmail(savedUser);
    }

    /**
     * Validates the uniqueness of the provided phone number.
     * Checks if there is any existing user with the same phone number.
     *
     * @param phoneNumber The phone number to be validated.
     * @throws AysUserAlreadyExistsByPhoneNumberException if the phone number is already associated with another user.
     */
    private void validatePhoneNumber(AysPhoneNumber phoneNumber) {
        userReadPort.findByPhoneNumber(phoneNumber)
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
                });
    }

    /**
     * Validates the uniqueness of the provided email address.
     * Checks if there is any existing user with the same email address.
     *
     * @param emailAddress The email address to be validated.
     * @throws AysUserAlreadyExistsByEmailException if the email address is already associated with another user.
     */
    private void validateEmailAddress(String emailAddress) {
        userReadPort.findByEmailAddress(emailAddress)
                .ifPresent(existingUser -> {
                    throw new AysUserAlreadyExistsByEmailException(emailAddress);
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
