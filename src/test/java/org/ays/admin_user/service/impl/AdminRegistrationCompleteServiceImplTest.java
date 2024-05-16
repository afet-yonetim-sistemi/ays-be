package org.ays.admin_user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminRegisterApplicationStatus;
import org.ays.admin_user.repository.AdminRegisterApplicationRepository;
import org.ays.admin_user.util.exception.AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.admin_user.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.admin_user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.PermissionEntityBuilder;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.RoleEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.repository.UserPasswordRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
    private AdminRegisterApplicationRepository adminRegisterApplicationRepository;


    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenFirstAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationCompleteRequest mockCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();
        AysPhoneNumberRequest mockPhoneNumber = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegisterApplicationEntity mockAdminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegisterApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(
                        mockPhoneNumber.getCountryCode(),
                        mockPhoneNumber.getLineNumber()
                ))
                .thenReturn(false);

        Mockito.when(roleRepository.findByInstitutionId(Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<PermissionEntity> mockPermissionEntities = List.of(
                new PermissionEntityBuilder().withValidFields().build()
        );
        Mockito.when(permissionRepository.findAll())
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

        AdminRegisterApplicationEntity mockCompletedVerificationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        mockCompletedVerificationEntity.complete(mockUserEntity.getId());
        Mockito.when(adminRegisterApplicationRepository
                        .save(Mockito.any(AdminRegisterApplicationEntity.class)))
                .thenReturn(mockCompletedVerificationEntity);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.times(1))
                .findByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAll();

        Mockito.verify(roleRepository, Mockito.times(1))
                .save(Mockito.any(RoleEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any(String.class));

        Mockito.verify(userPasswordRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.PasswordEntity.class));

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegisterApplicationEntity.class));
    }

    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationCompleteRequest mockCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();
        AysPhoneNumberRequest mockPhoneNumber = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegisterApplicationEntity mockAdminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegisterApplicationEntity));

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
        Mockito.when(roleRepository.findByInstitutionId(Mockito.anyString()))
                .thenReturn(Optional.of(mockRoleEntity));

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

        AdminRegisterApplicationEntity mockCompletedVerificationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        mockCompletedVerificationEntity.complete(mockUserEntity.getId());
        Mockito.when(adminRegisterApplicationRepository
                        .save(Mockito.any(AdminRegisterApplicationEntity.class)))
                .thenReturn(mockCompletedVerificationEntity);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(roleRepository, Mockito.times(1))
                .findByInstitutionId(Mockito.anyString());

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

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegisterApplicationEntity.class));
    }

    @Test
    void givenInvalidApplicationId_whenApplicationEntityNotFound_thenThrowAysAdminRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = "Invalid";
        AdminRegisterApplicationCompleteRequest mockCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenUsedApplicationId_whenApplicationEntityStatusIsNotWaiting_thenThrowAysAdminRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationCompleteRequest mockCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminRegisterApplicationEntity mockAdminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegisterApplicationStatus.COMPLETED)
                .build();
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockAdminRegisterApplicationEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
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

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));
    }

    @Test
    void givenExistingEmailFromAdminRegisterRequest_whenAdminExist_thenThrowAysAdminAlreadyExistsByEmailException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationCompleteRequest mockCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // When
        AdminRegisterApplicationEntity mockAdminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminRegisterApplicationEntity));

        Mockito.when(userRepository.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
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

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));
    }

    @Test
    void givenExistingPhoneNumberFromAdminRegisterRequest_whenAdminExist_thenThrowAysAdminAlreadyExistsByPhoneNumberException() {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder().withValidFields().build();
        AdminRegisterApplicationCompleteRequest mockAdminRegisterApplicationCompleteRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withEmail(AysValidTestData.EMAIL)
                .withPhoneNumber(mockPhoneNumber).build();

        AdminRegisterApplicationEntity mockAdminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        // When
        Mockito.when(adminRegisterApplicationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockAdminRegisterApplicationEntity));

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
                () -> adminUserRegisterService.complete(applicationId, mockAdminRegisterApplicationCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
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

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));
    }

}

