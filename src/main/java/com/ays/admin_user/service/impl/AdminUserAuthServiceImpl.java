package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterRequestToEntityMapper;
import com.ays.admin_user.repository.AdminUserRegisterVerificationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.admin_user.util.exception.*;
import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.organization.repository.OrganizationRepository;
import com.ays.organization.util.exception.AysOrganizationNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class AdminUserAuthServiceImpl implements AdminUserAuthService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserRegisterVerificationRepository adminUserRegisterVerificationRepository;
    private final AdminUserRegisterRequestToEntityMapper adminUserRegisterRequestToEntityMapper = AdminUserRegisterRequestToEntityMapper.initialize();

    private final OrganizationRepository organizationRepository;

    private final AysTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(final AdminUserRegisterRequest registerRequest) {

        final var verificationEntity = adminUserRegisterVerificationRepository.findById(registerRequest.getVerificationId())
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

//        TODO : check phone number!

        AdminUserEntity userEntityToBeSave = adminUserRegisterRequestToEntityMapper.mapForSaving(registerRequest, passwordEncoder);
        adminUserRepository.save(userEntityToBeSave);

        verificationEntity.complete(userEntityToBeSave.getId());
        adminUserRegisterVerificationRepository.save(verificationEntity);
    }

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
