package org.ays.auth.port.adapter;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.entity.AysPermissionEntity;
import org.ays.auth.model.entity.AysPermissionEntityBuilder;
import org.ays.auth.model.mapper.AysPermissionEntityToDomainMapper;
import org.ays.auth.repository.AysPermissionRepository;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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

        List<AysPermission> mockPermissions = permissionEntityToDomainMapper.map(mockPermissionEntities);

        // Then
        List<AysPermission> permissions = permissionAdapter.findAll();

        Assertions.assertEquals(mockPermissions, permissions);

        // Verify
        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAll();
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
        Mockito.when(permissionRepository.findAllByIsSuperFalse())
                .thenReturn(mockPermissionEntities);

        List<AysPermission> mockPermissions = permissionEntityToDomainMapper.map(mockPermissionEntities);

        // Then
        List<AysPermission> permissions = permissionAdapter.findAllByIsSuperFalse();

        Assertions.assertEquals(mockPermissions, permissions);
        permissions.forEach(permission -> Assertions.assertFalse(permission.isSuper()));

        // Verify
        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIsSuperFalse();
    }


    @Test
    void givenValidIds_whenAllPermissionEntitiesFoundByIds_thenReturnListOfPermissions() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        Set<String> mockIds = Set.of(mockId);

        // When
        List<AysPermissionEntity> mockPermissionEntities = List.of(
                new AysPermissionEntityBuilder()
                        .withValidValues()
                        .withId(mockId)
                        .build()
        );
        Mockito.when(permissionRepository.findAllById(mockIds))
                .thenReturn(mockPermissionEntities);

        List<AysPermission> mockPermissions = permissionEntityToDomainMapper.map(mockPermissionEntities);

        // Then
        List<AysPermission> permissions = permissionAdapter.findAllByIds(mockIds);

        Assertions.assertEquals(mockPermissions, permissions);

        // Verify
        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllById(mockIds);
    }

    @Test
    void givenValidIds_whenAllPermissionEntitiesNotFoundByIds_thenReturnEmptyList() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        Set<String> mockIds = Set.of(mockId);

        // When
        List<AysPermissionEntity> mockPermissionEntities = List.of();
        Mockito.when(permissionRepository.findAllById(mockIds))
                .thenReturn(mockPermissionEntities);

        List<AysPermission> mockPermissions = permissionEntityToDomainMapper.map(mockPermissionEntities);

        // Then
        List<AysPermission> permissions = permissionAdapter.findAllByIds(mockIds);

        Assertions.assertEquals(mockPermissions, permissions);

        // Verify
        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllById(mockIds);
    }

}
