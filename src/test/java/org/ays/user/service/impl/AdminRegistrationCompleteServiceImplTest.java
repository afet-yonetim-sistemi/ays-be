package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.AdminRegistrationApplicationEntityBuilder;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.PermissionEntityBuilder;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.RoleEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.user.repository.AdminRegistrationApplicationRepository;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.repository.UserPasswordRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.user.util.exception.AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.user.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

class AdminRegistrationCompleteServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminRegistrationCompleteServiceImpl adminUserRegisterService;

    @Mock
    private UserRepositoryV2 userRepository;

    @Mock
    private UserPasswordRepository userPasswordRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;


    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenFirstAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();
        AysPhoneNumberRequest mockPhoneNumber = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegistrationApplicationEntity mockAdminRegistrationApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegistrationApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(false);

        Mockito.when(roleRepository.findAllByInstitutionId(Mockito.anyString()))
                .thenReturn(Set.of());

        Set<PermissionEntity> mockPermissionEntities = Set.of(
                new PermissionEntityBuilder().withValidFields().build()
        );
        Mockito.when(permissionRepository.findAllByIsSuperFalse())
                .thenReturn(mockPermissionEntities);

        RoleEntity mockRoleEntity = new RoleEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(roleRepository.save(Mockito.any(RoleEntity.class)))
                .thenReturn(mockRoleEntity);

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();
        Mockito.when(userRepository.save(Mockito.any(UserEntityV2.class)))
                .thenReturn(mockUserEntity);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        UserEntityV2.PasswordEntity mockUserPasswordEntity = new UserEntityV2Builder.PasswordEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(userPasswordRepository.save(Mockito.any(UserEntityV2.PasswordEntity.class)))
                .thenReturn(mockUserPasswordEntity);

        AdminRegistrationApplicationEntity mockCompletedVerificationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        mockCompletedVerificationEntity.complete(mockUserEntity.getId());
        Mockito.when(adminRegistrationApplicationRepository
                        .save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockCompletedVerificationEntity);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIsSuperFalse();

        Mockito.verify(roleRepository, Mockito.times(1))
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any(String.class));

        Mockito.verify(userPasswordRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.PasswordEntity.class));

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }

    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();
        AysPhoneNumberRequest mockPhoneNumber = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegistrationApplicationEntity mockAdminRegistrationApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegistrationApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(false);

        RoleEntity mockRoleEntity = new RoleEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(roleRepository.findAllByInstitutionId(Mockito.anyString()))
                .thenReturn(Set.of(mockRoleEntity));

        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();
        Mockito.when(userRepository.save(Mockito.any(UserEntityV2.class)))
                .thenReturn(mockUserEntity);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        UserEntityV2.PasswordEntity mockUserPasswordEntity = new UserEntityV2Builder.PasswordEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(userPasswordRepository.save(Mockito.any(UserEntityV2.PasswordEntity.class)))
                .thenReturn(mockUserPasswordEntity);

        AdminRegistrationApplicationEntity mockCompletedVerificationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        mockCompletedVerificationEntity.complete(mockUserEntity.getId());
        Mockito.when(adminRegistrationApplicationRepository
                        .save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockCompletedVerificationEntity);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.never())
                .findAll();

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any(String.class));

        Mockito.verify(userPasswordRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.PasswordEntity.class));

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }

    @Test
    void givenInvalidApplicationId_whenApplicationEntityNotFound_thenThrowAysAdminRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = "Invalid";
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenUsedApplicationId_whenApplicationEntityStatusIsNotWaiting_thenThrowAysAdminRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminRegistrationApplicationEntity mockAdminRegistrationApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .build();
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegistrationApplicationEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.never())
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.never())
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(permissionRepository, Mockito.never())
                .save(Mockito.any(PermissionEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }

    @Test
    void givenExistingEmailFromAdminRegisterRequest_whenAdminExist_thenThrowAysAdminAlreadyExistsByEmailException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminRegistrationApplicationEntity mockAdminRegistrationApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminRegistrationApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.never())
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(permissionRepository, Mockito.never())
                .save(Mockito.any(PermissionEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }

    @Test
    void givenExistingPhoneNumberFromAdminRegisterRequest_whenAdminExist_thenThrowAysAdminAlreadyExistsByPhoneNumberException() {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder().withValidFields().build();
        AdminRegistrationApplicationCompleteRequest mockAdminRegistrationApplicationCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        AdminRegistrationApplicationEntity mockAdminRegistrationApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminRegistrationApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> adminUserRegisterService.complete(applicationId, mockAdminRegistrationApplicationCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(permissionRepository, Mockito.never())
                .save(Mockito.any(PermissionEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }

}

