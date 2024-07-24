package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
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
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.util.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

        Mockito.verify(identity, Mockito.times(mockRoles.size() + 1))
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
                AysUserAlreadyExistsByEmailException.class,
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

}