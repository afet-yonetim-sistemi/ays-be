package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysRolesNotExistException;
import org.ays.auth.exception.AysUserAlreadyActiveException;
import org.ays.auth.exception.AysUserAlreadyDeletedException;
import org.ays.auth.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.exception.AysUserAlreadyPassiveException;
import org.ays.auth.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.exception.AysUserNotExistByIdException;
import org.ays.auth.exception.AysUserNotPassiveException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class AysUserUpdateServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserUpdateServiceImpl userUpdateService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private AysIdentity identity;


    @Test
    void givenValidIdAndValidUpdateRequest_whenAllFieldsChanged_thenUpdateUser() {

        // Given
        String mockId = "3c57d56b-4a97-4f70-86a9-b4c9235cbe13";

        Set<String> mockRoleIds = Set.of(
                "00a07704-8d7c-4048-b001-9fb69b22bfe8",
                "7913e093-ae70-4029-ad8d-efbb21d79f26"
        );
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(mockRoleIds)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(userReadPort.findByPhoneNumber(Mockito.any(AysPhoneNumber.class)))
                .thenReturn(Optional.empty());

        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysRole> mockRoles = new ArrayList<>();
        mockUpdateRequest.getRoleIds().forEach(roleId -> {
            AysRole mockRole = new AysRoleBuilder()
                    .withValidValues()
                    .withId(roleId)
                    .withInstitution(mockInstitution)
                    .build();
            mockRoles.add(mockRole);
        });
        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockRoles);

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndValidUpdateRequest_whenRolesUpdatedOnly_thenUpdateUser() {

        // Given
        String mockId = "3c57d56b-4a97-4f70-86a9-b4c9235cbe13";

        Set<String> mockRoleIds = Set.of(
                "00a07704-8d7c-4048-b001-9fb69b22bfe8",
                "7913e093-ae70-4029-ad8d-efbb21d79f26"
        );
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(mockRoleIds)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withFirstName(mockUpdateRequest.getFirstName())
                .withLastName(mockUpdateRequest.getLastName())
                .withEmailAddress(mockUpdateRequest.getEmailAddress())
                .withPhoneNumber(AysPhoneNumber.builder().build())
                .withCity(mockUpdateRequest.getCity())
                .withRoles(List.of(
                        new AysRoleBuilder()
                                .withValidValues()
                                .build()
                ))
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        List<AysRole> mockRoles = new ArrayList<>();
        mockUpdateRequest.getRoleIds().forEach(roleId -> {
            AysRole mockRole = new AysRoleBuilder()
                    .withValidValues()
                    .withId(roleId)
                    .withInstitution(mockInstitution)
                    .build();
            mockRoles.add(mockRole);
        });
        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockRoles);

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndValidUpdateRequest_whenPhoneNumberChanged_thenUpdateUser() {

        // Given
        String mockId = "3c57d56b-4a97-4f70-86a9-b4c9235cbe13";

        Set<String> mockRoleIds = Set.of(
                "00a07704-8d7c-4048-b001-9fb69b22bfe8",
                "7913e093-ae70-4029-ad8d-efbb21d79f26"
        );
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(mockRoleIds)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        List<AysRole> mockRoles = new ArrayList<>();
        mockUpdateRequest.getRoleIds().forEach(roleId -> {
            AysRole mockRole = new AysRoleBuilder()
                    .withValidValues()
                    .withId(roleId)
                    .build();
            mockRoles.add(mockRole);
        });
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withFirstName(mockUpdateRequest.getFirstName())
                .withLastName(mockUpdateRequest.getLastName())
                .withEmailAddress(mockUpdateRequest.getEmailAddress())
                .withPhoneNumber(mockPhoneNumber)
                .withCity(mockUpdateRequest.getCity())
                .withRoles(mockRoles)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserNotFound_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "c92f3937-92ea-4e4b-81c5-11b88d61ef71";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserNotFoundForInstitution_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "c92f3937-92ea-4e4b-81c5-11b88d61ef71";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("d4eadfd3-3c13-4326-a571-99f4ffea5c92");

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserStatusIsNotActiveOrPassive_thenThrowAysUserIsNotActiveOrPassiveException() {

        // Given
        String mockId = "09e28825-42b9-426a-afb8-b849f549b36e";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.DELETED)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserIsNotActiveOrPassiveException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenPhoneNumberAlreadyInUse_thenThrowAysUserAlreadyExistsByPhoneNumberException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.ACTIVE)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();
        AysUser existingUserWithPhoneNumber = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userReadPort.findByPhoneNumber(mockPhoneNumber))
                .thenReturn(Optional.of(existingUserWithPhoneNumber));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenEmailAlreadyInUse_thenThrowAysUserAlreadyExistsByEmailException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "emailaddress@gmail.com";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withPhoneNumber(mockPhoneNumber)
                .withStatus(AysUserStatus.ACTIVE)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        AysUser existingUserWithEmail = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(mockEmailAddress))
                .thenReturn(Optional.of(existingUserWithEmail));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailAddressException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenRolesNotExist_thenThrowAysRolesNotExistException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withFirstName(mockUpdateRequest.getFirstName())
                .withLastName(mockUpdateRequest.getLastName())
                .withEmailAddress(mockUpdateRequest.getEmailAddress())
                .withPhoneNumber(mockPhoneNumber)
                .withCity(mockUpdateRequest.getCity())
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysRolesNotExistException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(userReadPort, Mockito.never())
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }


    @Test
    void givenValidId_whenUserIsPassive_thenActivateUser() {

        // Given
        String mockId = "21a0ab5a-c0e9-4789-9704-a6b5c02e2325";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.PASSIVE)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.activate(mockId);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserNotFound_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "9f1eb072-7830-4c43-9a32-d77b62ccddd3";

        // When
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserAlreadyActive_thenThrowUserAlreadyActiveException() {

        // Given
        String mockId = "bf7cc8d4-eab7-487d-8564-19be0f439b4a";

        // When
        Institution mockInstitution = new InstitutionBuilder()
            .withValidValues()
            .build();
        Mockito.when(identity.getInstitutionId())
            .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
            .withValidValues()
            .withId(mockId)
            .withInstitution(mockInstitution)
            .withStatus(AysUserStatus.ACTIVE)
            .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
            .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
            AysUserAlreadyActiveException.class,
            () -> userUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
            .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
            .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
            .save(Mockito.any(AysUser.class));
    }


    @Test
    void givenValidId_whenUserIsNotVerified_thenThrowAysUserIsNotPassiveException() {

        // Given
        String mockId = "2990c18b-4550-44c0-8247-4f8bb1cc67e1";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.NOT_VERIFIED)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserNotPassiveException.class,
                () -> userUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserIsDeleted_thenThrowAysUserIsNotPassiveException() {

        // Given
        String mockId = "9dc670d5-e9c7-454f-9c24-6a28748995c8";

        // When
        Institution mockInstitution = new InstitutionBuilder()
            .withValidValues()
            .build();
        Mockito.when(identity.getInstitutionId())
            .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
            .withValidValues()
            .withId(mockId)
            .withInstitution(mockInstitution)
            .withStatus(AysUserStatus.DELETED)
            .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
            .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
            AysUserNotPassiveException.class,
            () -> userUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
            .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
            .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
            .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenInstitutionIdDoesNotMatch_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "a785c6a2-229f-4a73-8e3a-3ff49bd16a07";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        Institution differentInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(differentInstitution)
                .withStatus(AysUserStatus.PASSIVE)
                .build();

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(userSavePort, Mockito.never())
                .save(mockUser);
    }


    @Test
    void givenValidId_whenUserFound_thenDeleteUser() {

        // Given
        String mockId = "90c509b5-c6fc-4161-a856-bb6f54d066d2";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.delete(mockId);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserAlreadyDeleted_thenThrowAysUserAlreadyDeletedException() {

        // Given
        String mockId = "3dac5c4c-adaa-4b2d-88ad-9cb65b1e86e8";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.DELETED)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        // Then
        Assertions.assertThrows(
                AysUserAlreadyDeletedException.class,
                () -> userUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserDoesNotFound_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "df6d0bc7-4fe7-496e-b8db-581c35cee402";

        // When
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserNotMatchedWithInstitution_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "cb3306e0-a36f-4af8-b0ae-b318bf0749fe";

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("0c916a8d-3d8b-48ad-a4b6-dfbe796058d3");

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }


    @Test
    void givenValidId_whenUserIsActive_thenPassivateUser() {

        // Given
        String mockId = "21a0ab5a-c0e9-4789-9704-a6b5c02e2325";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.ACTIVE)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(Mockito.mock(AysUser.class));

        // Then
        userUpdateService.passivate(mockId);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidUserId_whenUserNotFound_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "9f1eb072-7830-4c43-9a32-d77b62ccddd3";

        // When
        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidId_whenUserIsAlreadyPassive_thenThrowUserAlreadyPassiveException() {

        // Given
        String mockId = "b64ef470-6842-400f-ba23-2379c589095c";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.PASSIVE)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyPassiveException.class,
                () -> userUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @ParameterizedTest
    @EnumSource(value = AysUserStatus.class, names = {
            "NOT_VERIFIED",
            "DELETED"
    })
    void givenValidId_whenUserIsNotActive_thenThrowUserNotActiveException(AysUserStatus mockStatus) {

        // Given
        String mockId = "bf7cc8d4-eab7-487d-8564-19be0f439b4a";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(mockStatus)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserNotActiveException.class,
                () -> userUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidUserId_whenInstitutionIdDoesNotMatch_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = "a785c6a2-229f-4a73-8e3a-3ff49bd16a07";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("a9d5fdcf-9aea-41e4-9e00-fa607e9b6ab5")
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysUserStatus.ACTIVE)
                .build();

        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("85d51431-cc3d-465c-8c6c-1ecab336cc02");
        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(userSavePort, Mockito.never())
                .save(mockUser);
    }

}
