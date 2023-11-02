package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterRequestToAdminUserEntityMapper;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.admin_user.service.AdminUserRegisterService;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByEmailException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByPhoneNumberException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByUsernameException;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationCodeNotValidException;
import com.ays.common.model.AysPhoneNumber;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.institution.util.exception.AysInstitutionNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final InstitutionRepository institutionRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new admin user based on the given {@link AdminUserRegisterRequest} object. First, it retrieves
     * the {@link AdminUserRegisterApplicationEntity} associated with the verificationId provided in the request.
     * Then, it checks if the requested institution exists, if the email and username are unique, and finally
     * maps the request to an {@link AdminUserEntity} and saves it to the database. If successful, the verification
     * entity is completed and saved to the database.
     *
     * @param registerRequest the request object containing the information for the new admin user
     * @throws AysAdminUserRegisterApplicationCodeNotValidException if the verificationId provided is not valid
     * @throws AysInstitutionNotExistException                       if the requested institution does not exist
     * @throws AysAdminUserAlreadyExistsByEmailException             if an admin user with the same email already exists
     * @throws AysAdminUserAlreadyExistsByUsernameException          if an admin user with the same username already exists
     * @throws AysAdminUserAlreadyExistsByPhoneNumberException       if an admin user with the same phone number already exists
     */
    @Override
    @Transactional
    public void register(final AdminUserRegisterRequest registerRequest) {
        log.trace("Admin User Register Flow call starting for email of {}", registerRequest.getEmail());

        final AdminUserRegisterApplicationEntity verificationEntity = adminUserRegisterApplicationRepository
                .findById(registerRequest.getApplicationId())
                .filter(AdminUserRegisterApplicationEntity::isWaiting)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationCodeNotValidException(registerRequest.getApplicationId()));

        if (!institutionRepository.existsById(registerRequest.getInstitutionId())) {
            throw new AysInstitutionNotExistException(registerRequest.getInstitutionId());
        }

        if (adminUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AysAdminUserAlreadyExistsByEmailException(registerRequest.getEmail());
        }

        if (adminUserRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AysAdminUserAlreadyExistsByUsernameException(registerRequest.getUsername());
        }

        final AysPhoneNumber phoneNumber = registerRequest.getPhoneNumber();
        if (adminUserRepository.existsByCountryCodeAndLineNumber(phoneNumber.getCountryCode(), phoneNumber.getLineNumber())) {
            throw new AysAdminUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
        log.trace("Admin User Register Request checked successfully!");

        final AdminUserEntity userEntityToBeSave = adminUserRegisterRequestToAdminUserEntityMapper
                .mapForSaving(registerRequest, passwordEncoder.encode(registerRequest.getPassword()));

        adminUserRepository.save(userEntityToBeSave);
        log.trace("Admin User saved successfully!");

        verificationEntity.complete(userEntityToBeSave.getId());
        adminUserRegisterApplicationRepository.save(verificationEntity);
        log.trace("Admin User Register Verification complete successfully!");
    }

}
