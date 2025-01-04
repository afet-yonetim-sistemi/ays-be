package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysPermissionNotExistException;
import org.ays.auth.exception.AysRoleAlreadyExistsByNameException;
import org.ays.auth.exception.AysUserNotSuperAdminException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleCreateRequestBuilder;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Mockito.when(identity.getInstitutionId())
                .thenReturn("3ec91697-ebc9-42b2-8d4c-c0e792b027cb");

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
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

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

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
    void givenRoleCreateRequest_whenFieldsValid_thenCreateRole() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn("5e0e83da-73b2-4cbb-9753-10f17d63ab3b");

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
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

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

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
    void givenRoleCreateRequest_whenNameAlreadyExist_thenThrowAysRoleAlreadyExistsByNameException() {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn("04ba9be6-d1bc-4c0a-9188-336af6faa7a3");

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(AysRole.class)));

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyExistsByNameException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

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
        Mockito.when(identity.getInstitutionId())
                .thenReturn("0e1b24e2-bd74-4d3d-a419-a26a0e118d28");

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        List<AysPermission> mockPermissions = new ArrayList<>();
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
                .thenReturn("e3c9bbc3-bd45-4f46-9717-a38a8c94d593");

        // Then
        Assertions.assertThrows(
                AysUserNotSuperAdminException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

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
        Mockito.when(identity.getInstitutionId())
                .thenReturn("c38cc1dd-7474-463c-b93c-eeeae858fa35");

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysPermissionNotExistException.class,
                () -> roleCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

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