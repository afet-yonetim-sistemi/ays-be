package org.ays.auth.service.impl;

import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.request.AysPhoneNumberRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.util.exception.*;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class AysUserUpdateServiceImplTest {

    @InjectMocks
    private AysUserUpdateServiceImpl userUpdateService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysUserSavePort userSavePort;

    @Test
    void givenIdAndUserUpdateRequest_whenValuesValid_thenUpdateUser() {

        // Initialize
        List<AysRole> roles = roleReadPort.findAll();

        Set<String> roleIds = roles.stream()
                .map(AysRole::getId)
                .collect(Collectors.toSet());

        // Given
        String mockId = AysRandomUtil.generateUUID();

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withFirstName("newFirst")
                .withLastName("newSecond")
                .withEmailAddress("new@gmail.com")
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withCity("newCity")
                .withRoleIds(roleIds)
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .build();

        Mockito.when(userReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        List<AysRole> mockRoles = new ArrayList<>();
        mockUpdateRequest.getRoleIds().forEach(roleId -> {
            AysRole mockRole = new AysRoleBuilder()
                    .withValidValues()
                    .withId(roleId)
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

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userSavePort, Mockito.times(1)).save(Mockito.argThat(updatedUser ->
                updatedUser.getFirstName().equals("newFirst") &&
                        updatedUser.getLastName().equals("newSecond") &&
                        updatedUser.getEmailAddress().equals("new@gmail.com") &&
                        updatedUser.getPhoneNumber().getCountryCode().equals(mockUpdateRequest.getPhoneNumber().getCountryCode()) &&
                        updatedUser.getPhoneNumber().getLineNumber().equals(mockUpdateRequest.getPhoneNumber().getLineNumber()) &&
                        updatedUser.getCity().equals("newCity") &&
                        updatedUser.getRoles().equals(roles)
        ));

    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserNotFound_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
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

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserStatusIsInvalid_thenThrowAysUserIsNotActiveOrPassiveException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        AysUser mockUser = Mockito.mock(AysUser.class);
        Mockito.when(mockUser.isActive() || mockUser.isPassive()).thenReturn(false);

        // When
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

        AysUser mockUser = Mockito.mock(AysUser.class);
        Mockito.when(mockUser.isActive() || mockUser.isPassive()).thenReturn(true);

        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();

        AysUser existingUserWithPhoneNumber = Mockito.mock(AysUser.class);
        Mockito.when(existingUserWithPhoneNumber.getId()).thenReturn("anotherUserId");

        // When
        Mockito.when(userReadPort.findById(mockId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userReadPort.findByPhoneNumber(mockPhoneNumber)).thenReturn(Optional.of(existingUserWithPhoneNumber));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1)).findById(mockId);
        Mockito.verify(userReadPort, Mockito.times(1)).findByPhoneNumber(mockPhoneNumber);
        Mockito.verify(userSavePort, Mockito.never()).save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenEmailAlreadyInUse_thenThrowAysUserAlreadyExistsByEmailException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress("test@mail.com")
                .build();

        AysUser mockUser = Mockito.mock(AysUser.class);
        Mockito.when(mockUser.isActive() || mockUser.isPassive()).thenReturn(true);

        AysUser existingUserWithEmail = Mockito.mock(AysUser.class);
        Mockito.when(existingUserWithEmail.getId()).thenReturn("anotherUserId");

        // When
        Mockito.when(userReadPort.findById(mockId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userReadPort.findByEmailAddress("anothertest@gmail.com")).thenReturn(Optional.of(existingUserWithEmail));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1)).findById(mockId);
        Mockito.verify(userReadPort, Mockito.times(1)).findByEmailAddress(mockUpdateRequest.getEmailAddress());
        Mockito.verify(userSavePort, Mockito.never()).save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenRoleIdsDoNotExist_thenThrowAysRolesNotExistException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        Set<String> invalidRoleIds = Set.of("invalidRoleId1", "invalidRoleId2");
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(invalidRoleIds)
                .build();

        AysUser mockUser = Mockito.mock(AysUser.class);
        Mockito.when(mockUser.isActive() || mockUser.isPassive()).thenReturn(true);

        // When
        Mockito.when(userReadPort.findById(mockId)).thenReturn(Optional.of(mockUser));
        Mockito.when(roleReadPort.findAllByIds(invalidRoleIds)).thenReturn(new ArrayList<>());

        // Then
        Assertions.assertThrows(
                AysRolesNotExistException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1)).findById(mockId);
        Mockito.verify(roleReadPort, Mockito.times(1)).findAllByIds(invalidRoleIds);
        Mockito.verify(userSavePort, Mockito.never()).save(Mockito.any(AysUser.class));
    }

}
