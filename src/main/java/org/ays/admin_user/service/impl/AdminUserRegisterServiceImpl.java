package org.ays.admin_user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.model.mapper.AdminUserRegisterRequestToAdminUserEntityMapper;
import org.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.admin_user.service.AdminUserAuthService;
import org.ays.admin_user.service.AdminUserRegisterService;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByEmailException;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByPhoneNumberException;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByUsernameException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdAndStatusException;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.institution.repository.InstitutionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class implements the {@link AdminUserAuthService} interface and provides methods for
 * registering and authenticating admin users. It uses the {@link AdminUserRepository} and
 * {@link AdminUserRegisterApplicationRepository} for persistence operations and the
 * {@link AdminUserRegisterRequestToAdminUserEntityMapper} for mapping the request to entity objects.
 * It also uses the {@link InstitutionRepository} to check if the requested institution exists.
 * Authentication is handled using the {@link PasswordEncoder}
 */
@Slf4j
@Service
@RequiredArgsConstructor
class AdminUserRegisterServiceImpl implements AdminUserRegisterService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;
    private static final AdminUserRegisterRequestToAdminUserEntityMapper adminUserRegisterRequestToAdminUserEntityMapper = AdminUserRegisterRequestToAdminUserEntityMapper.initialize();

    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new admin user based on the given {@link AdminUserRegisterApplicationCompleteRequest} object. First, it retrieves
     * the {@link AdminUserRegisterApplicationEntity} associated with the applicationId provided as a parameter.
     * Then, it checks if the requested email and username are unique, and finally
     * maps the request to an {@link AdminUserEntity} and saves it to the database. If successful, the verification
     * entity is completed and saved to the database.
     *
     * @param applicationId the specified admin user register application id
     * @param request       the request object containing the information for the new admin user
     * @throws AysAdminUserRegisterApplicationNotExistByIdAndStatusException if the applicationId provided is not valid
     * @throws AysAdminUserAlreadyExistsByEmailException                     if an admin user with the same email already exists
     * @throws AysAdminUserAlreadyExistsByUsernameException                  if an admin user with the same username already exists
     * @throws AysAdminUserAlreadyExistsByPhoneNumberException               if an admin user with the same phone number already exists
     */
    @Override
    @Transactional
    public void completeRegistration(final String applicationId, final AdminUserRegisterApplicationCompleteRequest request) {
        log.trace("Admin User Register Flow call starting for email of {}", request.getEmail());

        final AdminUserRegisterApplicationEntity applicationEntity = adminUserRegisterApplicationRepository
                .findById(applicationId)
                .filter(AdminUserRegisterApplicationEntity::isWaiting)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationNotExistByIdAndStatusException(applicationId, AdminUserRegisterApplicationStatus.WAITING));

        if (adminUserRepository.existsByEmail(request.getEmail())) {
            throw new AysAdminUserAlreadyExistsByEmailException(request.getEmail());
        }

        if (adminUserRepository.existsByUsername(request.getUsername())) {
            throw new AysAdminUserAlreadyExistsByUsernameException(request.getUsername());
        }

        final AysPhoneNumberRequest phoneNumber = request.getPhoneNumber();
        if (adminUserRepository.existsByCountryCodeAndLineNumber(phoneNumber.getCountryCode(), phoneNumber.getLineNumber())) {
            throw new AysAdminUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
        log.trace("Admin User Register Request checked successfully!");

        final AdminUserEntity userEntityToBeSaved = adminUserRegisterRequestToAdminUserEntityMapper
                .mapForSaving(request, passwordEncoder.encode(request.getPassword()));
        userEntityToBeSaved.setInstitutionId(applicationEntity.getInstitutionId());

        adminUserRepository.save(userEntityToBeSaved);
        log.trace("Admin User saved successfully!");

        applicationEntity.complete(userEntityToBeSaved.getId());
        adminUserRegisterApplicationRepository.save(applicationEntity);
        log.trace("Admin User Register Verification complete successfully!");
    }

}
