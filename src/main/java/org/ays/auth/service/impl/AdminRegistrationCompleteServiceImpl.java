package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationCompleteRequestToUserMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AdminRegistrationCompleteService;
import org.ays.auth.util.exception.AdminRegistrationApplicationNotExistException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.institution.model.Institution;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link AdminRegistrationCompleteService} that handles the completion of admin registration applications.
 * This service manages the creation of new admin users and assigns appropriate roles and permissions.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AdminRegistrationCompleteServiceImpl implements AdminRegistrationCompleteService {

    private final AdminRegistrationApplicationReadPort adminRegistrationApplicationReadPort;
    private final AdminRegistrationApplicationSavePort adminRegistrationApplicationSavePort;

    private final AysUserSavePort userSavePort;
    private final AysUserReadPort userReadPort;
    private final AysRoleSavePort roleSavePort;
    private final AysRoleReadPort roleReadPort;
    private final AysPermissionReadPort permissionReadPort;

    private final PasswordEncoder passwordEncoder;

    private final AdminRegistrationApplicationCompleteRequestToUserMapper adminRegistrationApplicationCompleteRequestToUserMapper = AdminRegistrationApplicationCompleteRequestToUserMapper.initialize();

    /**
     * Completes the admin registration process by creating a new admin user, setting the user's institution,
     * roles, and permissions, encoding the password, and saving the registration application.
     *
     * @param id              The unique identifier of the admin registration application.
     * @param completeRequest The request containing necessary information to complete the registration.
     *                        This includes user details such as email, phone number, and password.
     * @throws AdminRegistrationApplicationNotExistException if the registration application does not exist or is not in a waiting state.
     * @throws AysUserAlreadyExistsByEmailAddressException          if a user with the given email already exists.
     * @throws AysUserAlreadyExistsByPhoneNumberException           if a user with the given phone number already exists.
     */
    @Override
    public void complete(final String id, final AdminRegistrationApplicationCompleteRequest completeRequest) {

        log.trace("Admin Register Flow call starting for email of {}", completeRequest.getEmailAddress());

        final AysUser user = adminRegistrationApplicationCompleteRequestToUserMapper.map(completeRequest);

        if (userReadPort.existsByEmailAddress(user.getEmailAddress())) {
            throw new AysUserAlreadyExistsByEmailAddressException(completeRequest.getEmailAddress());
        }

        if (userReadPort.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AysUserAlreadyExistsByPhoneNumberException(user.getPhoneNumber());
        }

        log.trace("Admin Registration Request checked successfully!");


        final AdminRegistrationApplication application = adminRegistrationApplicationReadPort
                .findById(id)
                .filter(AdminRegistrationApplication::isWaiting)
                .orElseThrow(() -> new AdminRegistrationApplicationNotExistException(id));

        user.setInstitution(application.getInstitution());
        user.notVerify();

        this.setAdminRole(user, application.getInstitution());

        final String encodedPassword = passwordEncoder.encode(user.getPassword().getValue());
        user.getPassword().setValue(encodedPassword);

        final AysUser savedUser = userSavePort.save(user);
        log.trace("Admin saved successfully!");

        application.complete(savedUser);
        adminRegistrationApplicationSavePort.save(application);

        log.trace("Admin Registration Verification complete successfully!");
    }

    /**
     * Sets the admin role for a user. If the institution has existing active roles, those roles are assigned.
     * Otherwise, a new admin role with appropriate permissions is created and assigned to the user.
     *
     * @param user        The {@link AysUser} who is being registered.
     * @param institution The {@link Institution} to which the user is being registered.
     */
    private void setAdminRole(final AysUser user,
                              final Institution institution) {

        final List<AysRole> rolesOfInstitution = roleReadPort.findAllActivesByInstitutionId(institution.getId());
        if (CollectionUtils.isNotEmpty(rolesOfInstitution)) {
            user.setRoles(rolesOfInstitution);
            return;
        }

        final List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();

        final AysRole role = AysRole.builder()
                .name("Admin")
                .institution(institution)
                .permissions(permissions)
                .status(AysRoleStatus.ACTIVE)
                .build();
        final AysRole savedRole = roleSavePort.save(role);

        user.setRoles(List.of(savedRole));

        log.trace("Admin Role created successfully!");
    }

}
