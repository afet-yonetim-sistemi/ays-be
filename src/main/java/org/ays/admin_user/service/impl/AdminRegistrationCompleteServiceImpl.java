package org.ays.admin_user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.admin_user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.admin_user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.admin_user.model.mapper.AdminRegistrationApplicationCompleteRequestToUserEntityMapper;
import org.ays.admin_user.repository.AdminRegistrationApplicationRepository;
import org.ays.admin_user.service.AdminRegistrationCompleteService;
import org.ays.admin_user.util.exception.AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.admin_user.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.admin_user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.repository.UserPasswordRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service implementation for handling the registration of admins.
 * This class is responsible for completing the registration process
 * by validating the request, checking for existing users, mapping the request
 * to a user entity, setting roles, and saving the user and password entities.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AdminRegistrationCompleteServiceImpl implements AdminRegistrationCompleteService {

    private final UserRepositoryV2 userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;

    private final PasswordEncoder passwordEncoder;

    private final AdminRegistrationApplicationCompleteRequestToUserEntityMapper adminRegistrationApplicationCompleteRequestToUserEntityMapper = AdminRegistrationApplicationCompleteRequestToUserEntityMapper.initialize();

    /**
     * Completes the registration process for an admin.
     * <p>
     * This method verifies the provided registration request, checks if the user already exists by email or phone number,
     * maps the registration request to a new user entity, sets the admin role, and saves the user and password entities.
     * Finally, it updates the registration application status to complete.
     * </p>
     *
     * @param id      the ID of the admin registration application
     * @param request the admin registration request containing user details
     * @throws AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException if the registration application does not exist or is not in a waiting status
     * @throws AysUserAlreadyExistsByEmailException                               if a user with the provided email already exists
     * @throws AysUserAlreadyExistsByPhoneNumberException                         if a user with the provided phone number already exists
     */
    @Override
    public void complete(final String id, final AdminRegistrationApplicationCompleteRequest request) {
        log.trace("Admin Register Flow call starting for email of {}", request.getEmailAddress());

        final AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository
                .findById(id)
                .filter(AdminRegistrationApplicationEntity::isWaiting)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException(id, AdminRegistrationApplicationStatus.WAITING));

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new AysUserAlreadyExistsByEmailException(request.getEmailAddress());
        }

        final AysPhoneNumberRequest phoneNumber = request.getPhoneNumber();
        if (userRepository.existsByCountryCodeAndLineNumber(phoneNumber.getCountryCode(), phoneNumber.getLineNumber())) {
            throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
        log.trace("Admin Registration Request checked successfully!");

        final UserEntityV2 userEntity = adminRegistrationApplicationCompleteRequestToUserEntityMapper
                .mapForSaving(request)
                .institutionId(applicationEntity.getInstitutionId())
                .build();
        this.setAdminRole(userEntity, applicationEntity.getInstitutionId());
        userRepository.save(userEntity);

        final UserEntityV2.PasswordEntity passwordEntity = UserEntityV2.PasswordEntity.builder()
                .userId(userEntity.getId())
                .value(passwordEncoder.encode(request.getPassword()))
                .build();
        userPasswordRepository.save(passwordEntity);
        log.trace("Admin saved successfully!");

        applicationEntity.complete(userEntity.getId());
        adminRegistrationApplicationRepository.save(applicationEntity);
        log.trace("Admin Registration Verification complete successfully!");
    }

    /**
     * Sets the admin role for the provided user entity.
     * <p>
     * This method checks if a role already exists for the institution. If it does, the role is assigned to the user.
     * If not, a new admin role is created with the appropriate permissions and assigned to the user.
     * </p>
     *
     * @param userEntity    the user entity to which the admin role is to be assigned
     * @param institutionId the ID of the institution for which the role is to be assigned
     */
    private void setAdminRole(final UserEntityV2 userEntity,
                              final String institutionId) {

        final Optional<RoleEntity> roleEntityFromDatabase = roleRepository
                .findByInstitutionId(institutionId);
        if (roleEntityFromDatabase.isPresent()) {
            userEntity.setRoles(Set.of(roleEntityFromDatabase.get()));
            return;
        }

        final Set<PermissionEntity> permissionEntities = permissionRepository.findAllByIsSuperFalse();

        final RoleEntity roleEntity = RoleEntity.builder()
                .name("Admin")
                .institutionId(institutionId)
                .permissions(permissionEntities)
                .build();
        roleRepository.save(roleEntity);

        userEntity.setRoles(Set.of(roleEntity));

        log.trace("Admin Role created successfully!");
    }

}
