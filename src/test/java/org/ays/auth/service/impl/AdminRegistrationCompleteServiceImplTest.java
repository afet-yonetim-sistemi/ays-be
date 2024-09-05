package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationBuilder;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

class AdminRegistrationCompleteServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AdminRegistrationCompleteServiceImpl adminUserRegisterService;


    @Mock
    private AdminRegistrationApplicationReadPort adminRegistrationApplicationReadPort;

    @Mock
    private AdminRegistrationApplicationSavePort adminRegistrationApplicationSavePort;


    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysRoleSavePort roleSavePort;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysPermissionReadPort permissionReadPort;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenFirstAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .build();
        AysPhoneNumberRequest mockPhoneNumberRequest = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .withoutUser()
                .build();
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockApplication));

        Mockito.when(userReadPort.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        AysPhoneNumber mockPhoneNumber = AysPhoneNumber.builder()
                .countryCode(mockPhoneNumberRequest.getCountryCode())
                .lineNumber(mockPhoneNumberRequest.getLineNumber())
                .build();
        Mockito.when(userReadPort.existsByPhoneNumber(mockPhoneNumber))
                .thenReturn(false);

        Mockito.when(roleReadPort.findAllActivesByInstitutionId(Mockito.anyString()))
                .thenReturn(List.of());

        List<AysPermission> mockPermissionEntities = List.of(
                new AysPermissionBuilder().withValidValues().build()
        );
        Mockito.when(permissionReadPort.findAllByIsSuperFalse())
                .thenReturn(mockPermissionEntities);

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .build();
        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(mockRole);

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockUser);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        AdminRegistrationApplication mockCompletedApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withId(mockApplicationId)
                .build();
        mockCompletedApplication.complete(mockUser);
        Mockito.when(adminRegistrationApplicationSavePort
                        .save(Mockito.any(AdminRegistrationApplication.class)))
                .thenReturn(mockApplication);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

    @Test
    void givenValidAdminRegisterApplicationCompleteRequest_whenAdminRegistered_thenDoNothing() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .build();
        AysPhoneNumberRequest mockPhoneNumberRequest = mockCompleteRequest.getPhoneNumber();

        // When
        AdminRegistrationApplication mockWaitingApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withId(mockApplicationId)
                .withoutUser()
                .build();
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockWaitingApplication));

        Mockito.when(userReadPort.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        AysPhoneNumber mockPhoneNumber = AysPhoneNumber.builder()
                .countryCode(mockPhoneNumberRequest.getCountryCode())
                .lineNumber(mockPhoneNumberRequest.getLineNumber())
                .build();
        Mockito.when(userReadPort.existsByPhoneNumber(mockPhoneNumber))
                .thenReturn(false);

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .build();
        Mockito.when(roleReadPort.findAllActivesByInstitutionId(Mockito.anyString()))
                .thenReturn(List.of(mockRole));

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockUser);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        AdminRegistrationApplication mockCompletedApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withId(mockApplicationId)
                .build();
        mockCompletedApplication.complete(mockUser);
        Mockito.when(adminRegistrationApplicationSavePort
                        .save(Mockito.any(AdminRegistrationApplication.class)))
                .thenReturn(mockCompletedApplication);

        // Then
        adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

    @Test
    void givenInvalidApplicationId_whenApplicationNotFound_thenThrowAysAdminRegistrationApplicationNotExistByIdException() {

        // Given
        String mockApplicationId = "Invalid";
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues().build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

    @Test
    void givenUsedApplicationId_whenApplicationStatusIsNotWaiting_thenThrowAysAdminRegistrationApplicationNotExistByIdException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .build();

        // When
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .build();
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockApplication));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

    @Test
    void givenExistingEmailFromAdminRegisterRequest_whenAdminExist_thenThrowAysUserAlreadyExistsByEmailException() {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .build();

        // When
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .build();
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockApplication));

        Mockito.when(userReadPort.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailAddressException.class,
                () -> adminUserRegisterService.complete(mockApplicationId, mockCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

    @Test
    void givenExistingPhoneNumberFromAdminRegisterRequest_whenAdminExist_thenThrowAysUserAlreadyExistsByPhoneNumberException() {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withValidValues()
                .build();

        AdminRegistrationApplicationCompleteRequest mockAdminRegistrationApplicationCompleteRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withEmailAddress(AysValidTestData.EMAIL_ADDRESS)
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(mockApplication));

        Mockito.when(userReadPort.existsByEmailAddress((Mockito.anyString())))
                .thenReturn(false);

        Mockito.when(userReadPort.existsByPhoneNumber(Mockito.any(AysPhoneNumber.class)))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> adminUserRegisterService.complete(applicationId, mockAdminRegistrationApplicationCompleteRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .existsByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllActivesByInstitutionId(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIsSuperFalse();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.any(String.class));

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));
    }

}

