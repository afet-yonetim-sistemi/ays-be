package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequestBuilder;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.entity.AdminUserRegisterVerificationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterVerificationEntityBuilder;
import com.ays.admin_user.repository.AdminUserRegisterVerificationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.auth.service.AysTokenService;
import com.ays.common.model.AysPhoneNumber;
import com.ays.organization.repository.OrganizationRepository;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AdminUserAuthServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserAuthServiceImpl adminUserAuthService;

    @Mock
    private AdminUserRepository adminUserRepository;
    @Mock
    private AdminUserRegisterVerificationRepository adminUserRegisterVerificationRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private AysTokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenValidAdminUserRegisterRequest_thenAdminUserRegistered() {

        // Given
        AysPhoneNumber mockPhoneNumber = AysTestData.VALID_PHONE_NUMBER;
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withEmail(AysTestData.VALID_EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

        Mockito.when(organizationRepository.existsById(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(false);
        Mockito.when(adminUserRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(adminUserRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(false);

        AdminUserEntity mockAdminUserEntityToBeSaved = new AdminUserEntityBuilder().build();
        Mockito.when(adminUserRepository.save(Mockito.any(AdminUserEntity.class)))
                .thenReturn(mockAdminUserEntityToBeSaved);

        mockAdminUserRegisterVerificationEntity.complete(mockAdminUserEntityToBeSaved.getId());
        Mockito.when(adminUserRegisterVerificationRepository.save(
                        Mockito.any(AdminUserRegisterVerificationEntity.class))
                )
                .thenReturn(mockAdminUserRegisterVerificationEntity);

        // Then
        adminUserAuthService.register(mockAdminUserRegisterRequest);

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(organizationRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserEntity.class));
        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterVerificationEntity.class));
    }
}