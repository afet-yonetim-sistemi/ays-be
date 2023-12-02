package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequestBuilder;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByEmailException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByPhoneNumberException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByUsernameException;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationCodeNotValidException;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.institution.util.exception.AysInstitutionNotExistException;
import com.ays.util.AysValidTestData;
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
    private InstitutionRepository institutionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenValidAdminUserRegisterRequest_thenAdminUserRegistered() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder().withValidFields().build();
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();


        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminUserRegisterApplicationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
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
        adminUserRegisterService.register(mockAdminUserRegisterRequest);

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
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
    void givenInvalidApplicationIdFromAdminUserRegisterRequest_whenApplicationEntityNotFound_thenThrowAysAdminUserRegisterApplicationCodeNotValidException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId("Invalid").build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationCodeNotValidException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenUsedApplicationIdFromAdminUserRegisterRequest_whenApplicationEntityStatusIsWaiting_thenThrowAysAdminUserRegisterApplicationCodeNotValidException() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder().withValidFields().build();
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity =
                new AdminUserRegisterApplicationEntityBuilder()
                        .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                        .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminUserRegisterApplicationEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationCodeNotValidException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionIdFromAdminUserRegisterRequest_whenInstitutionNotExist_thenThrowAysInstitutionNotExistException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId("Invalid").build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysInstitutionNotExistException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
    }

    @Test
    void givenExistingEmailFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByEmailException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByEmailException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
    }

    @Test
    void givenExistingUsernameFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByUsernameException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(adminUserRepository.existsByUsername((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByUsernameException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
    }

    @Test
    void givenExistingPhoneNumberFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByPhoneNumberException() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder().withValidFields().build();
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withApplicationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        AdminUserRegisterApplicationEntity mockAdminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterApplicationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(true);

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
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
    }

}

