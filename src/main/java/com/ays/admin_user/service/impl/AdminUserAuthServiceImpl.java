package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterVerificationEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterRequestToAdminUserEntityMapper;
import com.ays.admin_user.repository.AdminUserRegisterVerificationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.admin_user.util.exception.*;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.common.model.AysPhoneNumber;
import com.ays.organization.repository.OrganizationRepository;
import com.ays.organization.util.exception.AysOrganizationNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class implements the {@link AdminUserAuthService} interface and provides methods for
 * registering and authenticating admin users. It uses the {@link AdminUserRepository} and
 * {@link AdminUserRegisterVerificationRepository} for persistence operations and the
 * {@link AdminUserRegisterRequestToAdminUserEntityMapper} for mapping the request to entity objects.
 * It also uses the {@link OrganizationRepository} to check if the requested organization exists.
 * Authentication is handled using the {@link PasswordEncoder} and the {@link AysTokenService} is used for
 * generating and refreshing access tokens.
 */
@Service
@RequiredArgsConstructor
class AdminUserAuthServiceImpl implements AdminUserAuthService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserRegisterVerificationRepository adminUserRegisterVerificationRepository;
    private final AdminUserRegisterRequestToAdminUserEntityMapper adminUserRegisterRequestToAdminUserEntityMapper = AdminUserRegisterRequestToAdminUserEntityMapper.initialize();

    private final OrganizationRepository organizationRepository;

    private final AysTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new admin user based on the given {@link AdminUserRegisterRequest} object. First, it retrieves
     * the {@link AdminUserRegisterVerificationEntity} associated with the verificationId provided in the request.
     * Then, it checks if the requested organization exists, if the email and username are unique, and finally
     * maps the request to an {@link AdminUserEntity} and saves it to the database. If successful, the verification
     * entity is completed and saved to the database.
     *
     * @param registerRequest the request object containing the information for the new admin user
     * @throws AysAdminUserRegisterVerificationCodeNotValidException if the verificationId provided is not valid
     * @throws AysOrganizationNotExistException                      if the requested organization does not exist
     * @throws AysAdminUserAlreadyExistsByEmailException             if an admin user with the same email already exists
     * @throws AysAdminUserAlreadyExistsByUsernameException          if an admin user with the same username already exists
     */
    @Override
    public void register(final AdminUserRegisterRequest registerRequest) {

        final AdminUserRegisterVerificationEntity verificationEntity = adminUserRegisterVerificationRepository
                .findById(registerRequest.getVerificationId())
                .orElseThrow(() -> new AysAdminUserRegisterVerificationCodeNotValidException(registerRequest.getVerificationId()));

        if (!organizationRepository.existsById(registerRequest.getOrganizationId())) {
            throw new AysOrganizationNotExistException(registerRequest.getOrganizationId());
        }

        if (adminUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new AysAdminUserAlreadyExistsByEmailException(registerRequest.getEmail());
        }

        if (adminUserRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new AysAdminUserAlreadyExistsByUsernameException(registerRequest.getUsername());
        }

        final AysPhoneNumber phoneNumber = registerRequest.getPhoneNumber();
        if (adminUserRepository.findByCountryCodeAndLineNumber(phoneNumber.getCountryCode(), phoneNumber.getLineNumber()).isPresent()) {
            throw new AysAdminUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }

        final AdminUserEntity userEntityToBeSave = adminUserRegisterRequestToAdminUserEntityMapper.mapForSaving(registerRequest, passwordEncoder);
        adminUserRepository.save(userEntityToBeSave);

        verificationEntity.complete(userEntityToBeSave.getId());
        adminUserRegisterVerificationRepository.save(verificationEntity);
    }

    /**
     * Authenticates an admin user based on the given {@link AysLoginRequest} object. First, it retrieves the
     * {@link AdminUserEntity} associated with the provided username. Then, it checks if the user is active and
     * verified, and if the provided password matches the one stored in the database. If successful, an access
     * token is generated using the {@link AysTokenService} and returned.
     *
     * @param loginRequest the request object containing the username and password for authentication
     * @return an access token for the authenticated admin user
     * @throws UsernameNotFoundException        if an admin user with the provided username is not found
     * @throws AysAdminUserNotVerifiedException if the admin user is not verified
     * @throws AysAdminUserNotActiveException   if the admin user is not active
     * @throws PasswordNotValidException        if the provided password is not valid
     */
    @Override
    @Transactional
    public AysToken authenticate(final AysLoginRequest loginRequest) {

        final AdminUserEntity adminUserEntity = adminUserRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Admin User not found with username: " + loginRequest.getUsername()));

        if (!adminUserEntity.isActive()) {
            if (adminUserEntity.isNotVerified()) {
                throw new AysAdminUserNotVerifiedException(loginRequest.getUsername());
            }
            throw new AysAdminUserNotActiveException(loginRequest.getUsername());
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), adminUserEntity.getPassword())) {
            throw new PasswordNotValidException();
        }

        return tokenService.generate(adminUserEntity.getClaims());
    }

    /**
     * Refreshes an access token based on the provided refresh token. First, it verifies and validates the
     * refresh token using the {@link AysTokenService}. Then, it retrieves the {@link AdminUserEntity} associated
     * with the username stored in the refresh token's claims. If successful, a new access token is generated
     * using the {@link AysTokenService} and returned.
     *
     * @param refreshToken the refresh token used for generating a new access token
     * @return a new access token for the authenticated admin user
     * @throws AysAdminUserNotExistByUsernameException if an admin user with the username stored in the refresh
     *                                                 token's claims is not found
     */
    @Override
    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String username = tokenService
                .getClaims(refreshToken)
                .get(AysTokenClaims.USERNAME.getValue()).toString();

        final AdminUserEntity adminUserEntity = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new AysAdminUserNotExistByUsernameException(username));

        return tokenService.generate(adminUserEntity.getClaims(), refreshToken);
    }

}
