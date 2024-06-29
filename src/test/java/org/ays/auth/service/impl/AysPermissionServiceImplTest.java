package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.port.AysPermissionReadPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

class AysPermissionServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysPermissionServiceImpl permissionService;

    @Mock
    private AysPermissionReadPort permissionReadPort;

    @Mock
    private AysIdentity identity;


    @Test
    void whenPermissionsFoundIfUserIsSuperAdmin_thenReturnPermissionsWithSuperPermissions() {

        // When
        Mockito.when(identity.isSuperAdmin())
                .thenReturn(true);

        List<AysPermission> mockPermissions = List.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build()
        );
        Mockito.when(permissionReadPort.findAll())
                .thenReturn(mockPermissions);

        // Then
        List<AysPermission> permissions = permissionService.findAll();

        Assertions.assertEquals(mockPermissions.size(), permissions.size());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAll();
    }

    @Test
    void whenPermissionsFound_thenReturnPermissions() {

        // When
        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        List<AysPermission> mockPermissions = List.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build()
        );
        Mockito.when(permissionReadPort.findAllByIsSuperFalse())
                .thenReturn(mockPermissions);

        // Then
        List<AysPermission> permissions = permissionService.findAll();

        Assertions.assertEquals(mockPermissions.size(), permissions.size());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIsSuperFalse();
    }

}