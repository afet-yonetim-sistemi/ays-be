package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.Permission;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.PermissionEntityBuilder;
import org.ays.user.model.mapper.PermissionEntityToPermissionMapper;
import org.ays.user.repository.PermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

class PermissionServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private AysIdentity identity;


    private final PermissionEntityToPermissionMapper permissionEntityToPermissionMapper = PermissionEntityToPermissionMapper.initialize();


    @Test
    void whenPermissionsFoundIfUserIsSuperAdmin_thenReturnPermissionsWithSuperPermissions() {

        // When
        Mockito.when(identity.isSuperAdmin())
                .thenReturn(true);

        List<PermissionEntity> mockPermissionEntities = List.of(
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build()
        );
        Mockito.when(permissionRepository.findAll())
                .thenReturn(mockPermissionEntities);

        List<Permission> mockPermissions = permissionEntityToPermissionMapper.map(mockPermissionEntities);

        // Then
        List<Permission> permissions = permissionService.findAll();

        Assertions.assertEquals(mockPermissions.size(), permissions.size());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void whenPermissionsFound_thenReturnPermissions() {

        // When
        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        Set<PermissionEntity> mockPermissionEntities = Set.of(
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build(),
                new PermissionEntityBuilder().withValidFields().build()
        );
        Mockito.when(permissionRepository.findAllByIsSuperFalse())
                .thenReturn(mockPermissionEntities);

        List<Permission> mockPermissions = permissionEntityToPermissionMapper.map(mockPermissionEntities);

        // Then
        List<Permission> permissions = permissionService.findAll();

        Assertions.assertEquals(mockPermissions.size(), permissions.size());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIsSuperFalse();
    }

}