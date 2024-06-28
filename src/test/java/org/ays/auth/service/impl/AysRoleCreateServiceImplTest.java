package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleCreateRequestBuilder;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.util.exception.AysPermissionNotExistException;
import org.ays.auth.util.exception.AysRoleAlreadyExistsByNameException;
import org.ays.auth.util.exception.AysUserNotSuperAdminException;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class AysRoleCreateServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysRoleCreateServiceImpl roleCreateService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysRoleSavePort roleSavePort;

    @Mock
    private AysPermissionReadPort permissionReadPort;

    @Mock
    private AysIdentity identity;

    @Test
    void givenRoleCreateRequest_whenFieldsValid_thenCreateSuperRole() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<AysPermission> mockPermissions = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
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

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenRoleCreateRequest_whenFieldsValid_thenCreateRole() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<AysPermission> mockPermissions = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
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
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenRoleCreateRequest_whenNameAlreadyExist_thenThrowAysRoleAlreadyExistsByNameException() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(AysRole.class)));

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyExistsByNameException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
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
    void givenRoleCreateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenThrowAysUserNotSuperAdminException() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<AysPermission> mockPermissions = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
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
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
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
    void givenRoleCreateRequest_whenPermissionsNotExists_thenThrowAysPermissionNotExistException() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(roleReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(Set.of());

        // Then
        Assertions.assertThrows(
                AysPermissionNotExistException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
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

}