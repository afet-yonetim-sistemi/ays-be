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
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByEmailException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByPhoneNumberException;
import com.ays.admin_user.util.exception.AysAdminUserAlreadyExistsByUsernameException;
import com.ays.admin_user.util.exception.AysAdminUserRegisterVerificationCodeNotValidException;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.institution.util.exception.AysInstitutionNotExistException;
import com.ays.util.AysTestData;
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
    private AdminUserRegisterVerificationRepository adminUserRegisterVerificationRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenValidAdminUserRegisterRequest_thenAdminUserRegistered() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder().withValidFields().build();
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withVerificationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysTestData.VALID_EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

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

        assert mockAdminUserRegisterVerificationEntity != null;
        mockAdminUserRegisterVerificationEntity.complete(mockAdminUserEntityToBeSaved.getId());
        Mockito.when(adminUserRegisterVerificationRepository.save(
                        Mockito.any(AdminUserRegisterVerificationEntity.class))
                )
                .thenReturn(mockAdminUserRegisterVerificationEntity);

        // Then
        adminUserRegisterService.register(mockAdminUserRegisterRequest);

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserEntity.class));
        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterVerificationEntity.class));
    }

    @Test
    void givenInvalidVerificationIdFromAdminUserRegisterRequest_whenVerificationEntityNotFound_thenThrowAysAdminUserRegisterVerificationCodeNotValidException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withVerificationId("Invalid").build();

        // When
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterVerificationCodeNotValidException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionIdFromAdminUserRegisterRequest_whenInstitutionNotExist_thenThrowAysInstitutionNotExistException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withVerificationId(AysRandomUtil.generateUUID())
                .withInstitutionId("Invalid").build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysInstitutionNotExistException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
    }

    @Test
    void givenExistingEmailFromAdminUserRegisterRequest_whenAdminUserExist_thenThrowAysAdminUserAlreadyExistsByEmailException() {

        // Given
        AdminUserRegisterRequest mockAdminUserRegisterRequest = new AdminUserRegisterRequestBuilder()
                .withVerificationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysTestData.VALID_EMAIL).build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

        Mockito.when(institutionRepository.existsById(Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(adminUserRepository.existsByEmail((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysAdminUserAlreadyExistsByEmailException.class,
                () -> adminUserRegisterService.register(mockAdminUserRegisterRequest)
        );

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
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
                .withVerificationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysTestData.VALID_EMAIL).build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

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

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
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
                .withVerificationId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withEmail(AysTestData.VALID_EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        // When
        AdminUserRegisterVerificationEntity mockAdminUserRegisterVerificationEntity = new AdminUserRegisterVerificationEntityBuilder().build();
        Mockito.when(adminUserRegisterVerificationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminUserRegisterVerificationEntity));

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

        Mockito.verify(adminUserRegisterVerificationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsById(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByEmail(Mockito.anyString());
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .existsByUsername(Mockito.anyString());
    }

}

