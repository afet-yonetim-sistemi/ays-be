package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.request.AysRoleUpdateRequest;
import org.ays.auth.model.request.AysRoleUpdateRequestBuilder;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.util.exception.*;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class AysRoleUpdateServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysRoleUpdateServiceImpl roleUpdateService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysRoleSavePort roleSavePort;

    @Mock
    private AysPermissionReadPort permissionReadPort;

    @Mock
    private AysIdentity identity;

    @Test
    void givenIdAndRoleUpdateRequest_whenValuesValid_thenUpdateRoleWithSuperPermissions() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
        mockUpdateRequest.getPermissionIds().forEach(permissionId -> {
            AysPermission mockPermission = new AysPermissionBuilder()
                    .withValidValues()
                    .withId(permissionId)
                    .withIsSuper(true)
                    .build();
            mockPermissions.add(mockPermission);
        });
        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockPermissions);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(true);

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenIdAndRoleUpdateRequest_whenValuesValid_thenUpdateRole() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
        mockUpdateRequest.getPermissionIds().forEach(permissionId -> {
            AysPermission mockPermission = new AysPermissionBuilder()
                    .withValidValues()
                    .withId(permissionId)
                    .withIsSuper(false)
                    .build();
            mockPermissions.add(mockPermission);
        });
        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockPermissions);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenRoleNotFound_thenThrowAysRoleNotExistByIdException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.never())
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenNameAlreadyExist_thenThrowAysRoleAlreadyExistsByNameException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(AysRole.class)));

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyExistsByNameException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.never())
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenThrowAysUserNotSuperAdminException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
        mockUpdateRequest.getPermissionIds().forEach(permissionId -> {
            AysPermission mockPermission = new AysPermissionBuilder()
                    .withValidValues()
                    .withId(permissionId)
                    .withIsSuper(true)
                    .build();
            mockPermissions.add(mockPermission);
        });
        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockPermissions);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        Mockito.when(identity.getUserId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        Assertions.assertThrows(
                AysUserNotSuperAdminException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }


    @Test
    void givenValidIdAndRoleUpdateRequest_whenPermissionsNotExists_thenThrowAysPermissionNotExistException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysPermissionNotExistException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.never())
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidId_whenRoleIsNotPassive_thenActivateRole() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withStatus(AysRoleStatus.PASSIVE)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.activate(mockId);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));

    }

    @Test
    void givenValidId_whenRoleIsPassive_thenThrowAysInvalidRoleStatusException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withStatus(AysRoleStatus.ACTIVE)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));


        // Then
        Assertions.assertThrows(
                AysInvalidRoleStatusException.class,
                () -> roleUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

    }

    @Test
    void givenValidId_whenRoleNotFound_thenThrowAysRoleNotExistByIdException() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

}
