package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysPhoneNumberRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.util.exception.AysUserIsNotActiveOrPassiveException;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class AysUserUpdateServiceImplTest extends AysUnitTest {

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
    void givenValidIdAndUserUpdateRequest_whenUserStatusIsNotActiveOrPassive_thenThrowAysUserIsNotActiveOrPassiveException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.DELETED)
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
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.ACTIVE)
                .build();

        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode(mockUpdateRequest.getPhoneNumber().getCountryCode())
                .withLineNumber(mockUpdateRequest.getPhoneNumber().getLineNumber())
                .build();

        AysUser existingUserWithPhoneNumber = new AysUserBuilder()
                .withValidValues()
                .build();

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
        String mockEmailAddress = "emailaddress@gmail.com";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.ACTIVE)
                .build();

        AysUser existingUserWithEmail = new AysUserBuilder()
                .withValidValues()
                .build();

        Mockito.when(userReadPort.findById(mockId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userReadPort.findByEmailAddress(mockEmailAddress)).thenReturn(Optional.of(existingUserWithEmail));

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
    void givenValidIdAndUserUpdateRequest_whenRolesNotExist_thenThrowAysRolesNotExistException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
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

        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysRolesNotExistException.class,
                () -> userUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

}
