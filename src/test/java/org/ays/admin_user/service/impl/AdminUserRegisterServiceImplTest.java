package org.ays.admin_user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByEmailException;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByPhoneNumberException;
import org.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByUsernameException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdAndStatusException;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AdminUserRegisterServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserRegisterServiceImpl adminUserRegisterService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenValidAdminUserRegisterApplicationCompleteRequest_whenUserRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationCompleteRequest mockCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();
        AysPhoneNumberRequest mockPhoneNumber = mockCompleteRequest.getPhoneNumber();

        // When
        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminUserRegisterApplicationEntity));

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(false);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        AdminUserEntity mockAdminUserEntityToBeSaved = new AdminUserEntityBuilder().build();
        Mockito.when(adminUserRepository.save(Mockito.any(AdminUserEntity.class)))
                .thenReturn(mockAdminUserEntityToBeSaved);

        AdminUserRegisterApplicationEntity mockCompletedVerificationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        mockCompletedVerificationEntity.complete(mockAdminUserEntityToBeSaved.getId());
        Mockito.when(adminUserRegisterApplicationRepository
                        .save(Mockito.any(AdminUserRegisterApplicationEntity.class)))
                .thenReturn(mockCompletedVerificationEntity);

        // Then
        adminUserRegisterService.completeRegistration(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserEntity.class));
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
    }

    @Test
    void givenInvalidApplicationId_whenApplicationEntityNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = "Invalid";
        AdminUserRegisterApplicationCompleteRequest mockCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterService.completeRegistration(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenUsedApplicationId_whenApplicationEntityStatusIsNotWaiting_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationCompleteRequest mockCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                .build();
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminUserRegisterApplicationEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterService.completeRegistration(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenExistingEmailFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByEmailException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationCompleteRequest mockCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByEmailException.class,
                () -> adminUserRegisterService.completeRegistration(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
    }

    @Test
    void givenExistingUsernameFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByUsernameException() {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationCompleteRequest mockAdminUserRegisterApplicationCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withEmail(AysValidTestData.EMAIL).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByUsername((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByUsernameException.class,
                () -> adminUserRegisterService.completeRegistration(applicationId, mockAdminUserRegisterApplicationCompleteRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
    }

    @Test
    void givenExistingPhoneNumberFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByPhoneNumberException() {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder().withValidFields().build();
        AdminUserRegisterApplicationCompleteRequest mockAdminUserRegisterApplicationCompleteRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByUsername((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByPhoneNumberException.class,
                () -> adminUserRegisterService.completeRegistration(applicationId, mockAdminUserRegisterApplicationCompleteRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
    }

}

