package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.entity.AysPermissionEntity;
import org.ays.auth.model.entity.AysPermissionEntityBuilder;
import org.ays.auth.model.mapper.AysPermissionEntityToDomainMapper;
import org.ays.auth.repository.AysPermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AysPermissionAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysPermissionAdapter permissionAdapter;

    @Mock
    private AysPermissionRepository permissionRepository;


    private final AysPermissionEntityToDomainMapper permissionEntityToDomainMapper = AysPermissionEntityToDomainMapper.initialize();


    @Test
    void whenAllPermissionEntitiesFound_thenReturnSetOfPermissions() {

        // When
        List<AysPermissionEntity> mockPermissionEntities = List.of(
                new AysPermissionEntityBuilder()
                        .withValidValues()
                        .build(),
                new AysPermissionEntityBuilder()
                        .withValidValues()
                        .build()
        );
        Mockito.when(permissionRepository.findAll())
                .thenReturn(mockPermissionEntities);

        Set<AysPermission> mockPermissions = new HashSet<>(
                permissionEntityToDomainMapper.map(mockPermissionEntities)
        );

        // Then
        Set<AysPermission> permissions = permissionAdapter.findAll();

        Assertions.assertEquals(mockPermissions, permissions);
    }


    @Test
    void whenAllPermissionEntitiesFoundByIsSuperFalse_thenReturnSetOfPermissions() {

        // When
        List<AysPermissionEntity> mockPermissionEntities = List.of(
                new AysPermissionEntityBuilder()
                        .withValidValues()
                        .withIsSuper(false)
                        .build(),
                new AysPermissionEntityBuilder()
                        .withValidValues()
                        .withIsSuper(false)
                        .build()
        );
        Mockito.when(permissionRepository.findAll())
                .thenReturn(mockPermissionEntities);

        Set<AysPermission> mockPermissions = new HashSet<>(
                permissionEntityToDomainMapper.map(mockPermissionEntities)
        );

        // Then
        Set<AysPermission> permissions = permissionAdapter.findAll();

        Assertions.assertEquals(mockPermissions, permissions);
        permissions.forEach(permission -> Assertions.assertFalse(permission.isSuper()));
    }

}
