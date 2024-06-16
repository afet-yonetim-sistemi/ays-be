package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.dto.request.RoleCreateRequestBuilder;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.PermissionEntityBuilder;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.request.RoleCreateRequest;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.util.exception.AysPermissionNotExistException;
import org.ays.user.util.exception.AysRoleAlreadyExistsByNameException;
import org.ays.user.util.exception.AysUserNotSuperAdminException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class RoleCreateServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private RoleCreateServiceImpl roleCreateService;

    @Mock
    private AysIdentity identity;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Test
    void givenRoleCreateRequest_whenFieldsValid_thenCreateSuperRole() {
        // Given
        RoleCreateRequest mockCreateRequest = new RoleCreateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<PermissionEntity> mockPermissionEntities = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
            PermissionEntity permissionEntity = new PermissionEntityBuilder()
                    .withValidFields()
                    .withId(permissionId)
                    .withIsSuper(true)
                    .build();
            mockPermissionEntities.add(permissionEntity);
        });
        Mockito.when(permissionRepository.findAllByIdIn(Mockito.anySet()))
                .thenReturn(mockPermissionEntities);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(true);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        Mockito.when(roleRepository.save(Mockito.any(RoleEntity.class)))
                .thenReturn(Mockito.mock(RoleEntity.class));

        // Then
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIdIn(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleRepository, Mockito.times(1))
                .save(Mockito.any(RoleEntity.class));
    }

    @Test
    void givenRoleCreateRequest_whenFieldsValid_thenCreateRole() {
        // Given
        RoleCreateRequest mockCreateRequest = new RoleCreateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<PermissionEntity> mockPermissionEntities = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
            PermissionEntity permissionEntity = new PermissionEntityBuilder()
                    .withValidFields()
                    .withId(permissionId)
                    .withIsSuper(false)
                    .build();
            mockPermissionEntities.add(permissionEntity);
        });
        Mockito.when(permissionRepository.findAllByIdIn(Mockito.anySet()))
                .thenReturn(mockPermissionEntities);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        Mockito.when(roleRepository.save(Mockito.any(RoleEntity.class)))
                .thenReturn(Mockito.mock(RoleEntity.class));

        // Then
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIdIn(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleRepository, Mockito.times(1))
                .save(Mockito.any(RoleEntity.class));
    }

    @Test
    void givenRoleCreateRequest_whenNameAlreadyExist_thenThrowAysRoleAlreadyExistsByNameException() {
        // Given
        RoleCreateRequest mockCreateRequest = new RoleCreateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(RoleEntity.class)));

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyExistsByNameException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.never())
                .findAllByIdIn(Mockito.anySet());

        Mockito.verify(identity, Mockito.never())
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));
    }

    @Test
    void givenRoleCreateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenThrowAysUserNotSuperAdminException() {
        // Given
        RoleCreateRequest mockCreateRequest = new RoleCreateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Set<PermissionEntity> mockPermissionEntities = new HashSet<>();
        mockCreateRequest.getPermissionIds().forEach(permissionId -> {
            PermissionEntity permissionEntity = new PermissionEntityBuilder()
                    .withValidFields()
                    .withId(permissionId)
                    .withIsSuper(true)
                    .build();
            mockPermissionEntities.add(permissionEntity);
        });
        Mockito.when(permissionRepository.findAllByIdIn(Mockito.anySet()))
                .thenReturn(mockPermissionEntities);

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
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIdIn(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));
    }


    @Test
    void givenRoleCreateRequest_whenPermissionsNotExists_thenThrowAysPermissionNotExistException() {
        // Given
        RoleCreateRequest mockCreateRequest = new RoleCreateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(permissionRepository.findAllByIdIn(Mockito.anySet()))
                .thenReturn(Set.of());

        // Then
        Assertions.assertThrows(
                AysPermissionNotExistException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(permissionRepository, Mockito.times(1))
                .findAllByIdIn(Mockito.anySet());

        Mockito.verify(identity, Mockito.never())
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.never())
                .getUserId();

        Mockito.verify(roleRepository, Mockito.never())
                .save(Mockito.any(RoleEntity.class));
    }

}