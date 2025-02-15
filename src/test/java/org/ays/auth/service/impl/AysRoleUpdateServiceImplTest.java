package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysInvalidRoleStatusException;
import org.ays.auth.exception.AysPermissionNotExistException;
import org.ays.auth.exception.AysRoleAlreadyDeletedException;
import org.ays.auth.exception.AysRoleAlreadyExistsByNameException;
import org.ays.auth.exception.AysRoleAssignedToUserException;
import org.ays.auth.exception.AysRoleNotExistByIdException;
import org.ays.auth.exception.AysUserNotSuperAdminException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.request.AysRoleUpdateRequest;
import org.ays.auth.model.request.AysRoleUpdateRequestBuilder;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        String mockId = "1390ac31-53df-4ded-b2a5-e7322fb02c4b";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("efaf3cfe-196e-4d45-846b-659a90f416e6")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
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
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenIdAndRoleUpdateRequest_whenValuesValid_thenUpdateRole() {
        // Given
        String mockId = "7e85eaf9-f96b-46b6-bcf0-bb883a890010";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("c3678b17-4d06-47f8-be75-663535f02c7b")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
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

        Mockito.when(identity.getUserId())
                .thenReturn("148b1d65-a0a6-458d-b98c-20fad8594487");

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenIdAndRoleUpdateRequest_whenPermissionsUpdatedAndNameUnchanged_thenUpdateRole() {
        // Given
        String mockId = "73854263-649b-47af-af56-43962ab77087";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withName("ExistingName")
                .withPermissionIds(Set.of("b8d3ddce-45ca-4017-b52f-5826e330a1e6"))
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("2d47c593-129f-4c32-a282-d9f132cebc55")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        List<AysPermission> mockOldPermissions = List.of(
                new AysPermissionBuilder()
                        .withValidValues()
                        .withName("institution:page")
                        .withCategory(AysPermissionCategory.PAGE)
                        .build()
        );
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withName("ExistingName")
                .withPermissions(mockOldPermissions)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        List<AysPermission> mockNewPermissions = new ArrayList<>();
        mockUpdateRequest.getPermissionIds().forEach(permissionId -> {
            AysPermission mockPermission = new AysPermissionBuilder()
                    .withValidValues()
                    .withId(permissionId)
                    .withIsSuper(false)
                    .build();
            mockNewPermissions.add(mockPermission);
        });
        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockNewPermissions);

        Mockito.when(identity.isSuperAdmin())
                .thenReturn(false);

        Mockito.when(identity.getUserId())
                .thenReturn("8a11623c-8ab4-4af9-a55c-5d3d338126d7");

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.update(mockId, mockUpdateRequest);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(permissionReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(identity, Mockito.times(1))
                .isSuperAdmin();

        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenRoleNotFound_thenThrowRoleNotExistByIdException() {
        // Given
        String mockId = "9b06ab7e-602b-48e6-95cb-fb20cb883f9e";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn("39ad1713-5ee1-4fcd-b93e-bae1f77d03d1");

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
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
    void givenValidIdAndRoleUpdateRequest_whenRoleNotMatchedWithInstitution_thenThrowRoleNotExistByIdException() {
        // Given
        String mockId = "ddfbb4c9-d74a-4fcd-9312-4c4a336f7882";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockFirstInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("fe22a00a-3ba0-4c76-a6f9-a627e044a641")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockFirstInstitution.getId());

        Institution mockSecondInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("134742f1-2c60-41c0-9b89-eca72647e15a")
                .build();
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockSecondInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.never())
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
    void givenValidIdAndRoleUpdateRequest_whenNameAlreadyExist_thenThrowRoleAlreadyExistsByNameException() {
        // Given
        String mockId = "dd750c12-00b3-46ef-9aa3-b6e312bbafdb";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("95d6acfc-5ded-41e2-82f5-44c28325e62d")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(AysRole.class)));

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyExistsByNameException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

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
    void givenValidIdAndRoleUpdateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenThrowUserNotSuperAdminException() {
        // Given
        String mockId = "9fc336e5-5ea7-4c6f-8e3e-7c74c7717a26";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("7174a550-9bbb-413e-aba7-7c79cf51a553")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
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

        // Then
        Assertions.assertThrows(
                AysUserNotSuperAdminException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

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
    void givenValidIdAndRoleUpdateRequest_whenPermissionsNotExists_thenThrowPermissionNotExistException() {
        // Given
        String mockId = "a2431a45-4632-4374-8c09-2a403ddbfd3a";
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId("abe738b5-55e4-45e1-b487-cd0f20995623")
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleReadPort.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(permissionReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysPermissionNotExistException.class,
                () -> roleUpdateService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

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


    @Test
    void givenValidId_whenRoleIsPassive_thenActivateRole() {

        // Given
        String mockId = "c1614e48-725f-4c5f-88e8-4442332d0f57";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysRoleStatus.PASSIVE)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.activate(mockId);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ACTIVE",
            "DELETED"
    })
    void givenValidId_whenRoleIsNotPassive_thenThrowInvalidRoleStatusException(String roleStatus) {
        // Initialize
        AysRoleStatus status = AysRoleStatus.valueOf(roleStatus);

        // Given
        String mockId = "f0229414-0179-4a56-bead-1ce03e168539";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(status)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        // Then
        Assertions.assertThrows(
                AysInvalidRoleStatusException.class,
                () -> roleUpdateService.activate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidId_whenRoleNotFoundForActivation_thenThrowRoleNotExistByIdException() {

        // Given
        String mockId = "f6ecfa12-17e0-4294-a8fb-6598224fcd93";

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

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();
    }


    @Test
    void givenValidId_whenRoleIsActive_thenPassivateRole() {

        // Given
        String mockId = "6f3db9cd-2e08-45a6-bcda-707bef233b4e";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(AysRoleStatus.ACTIVE)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.passivate(mockId);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PASSIVE",
            "DELETED"
    })
    void givenValidId_whenRoleIsNotActive_thenThrowInvalidRoleStatusException(String roleStatus) {

        // Initialize
        AysRoleStatus status = AysRoleStatus.valueOf(roleStatus);

        // Given
        String mockId = "f35b6751-613e-47c2-b48f-12cf57207648";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(mockInstitution)
                .withStatus(status)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        // Then
        Assertions.assertThrows(
                AysInvalidRoleStatusException.class,
                () -> roleUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

    }

    @Test
    void givenValidId_whenRoleNotFoundForPassivation_thenThrowRoleNotExistByIdException() {

        // Given
        String mockId = "d0dc24fc-a2d2-4e31-b482-489f64b0cbf6";

        // When
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();
    }

    @Test
    void givenValidId_whenInstitutionIdDoesNotMatch_thenThrowRoleNotExistByIdException() {

        // Given
        String mockId = "666e4d1b-58ac-486e-b373-189b4f354e36";

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        Institution differentInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withInstitution(differentInstitution)
                .withStatus(AysRoleStatus.ACTIVE)
                .build();

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        Mockito.when(roleReadPort.findById(mockId))
                .thenReturn(Optional.of(mockRole));

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(roleSavePort, Mockito.never())
                .save(mockRole);
    }

    @Test
    void givenValidIdForPassivation_whenRoleUsing_thenThrowRoleAssignedToUserException() {

        // Given
        String mockId = "731f4ba4-c34b-41c3-b488-d9a0c69904a3";

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockRole.getInstitution().getId());

        Mockito.when(roleReadPort.isRoleUsing(Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysRoleAssignedToUserException.class,
                () -> roleUpdateService.passivate(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .isRoleUsing(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }


    @Test
    void givenValidId_whenRoleFound_thenDeleteRole() {

        // Given
        String mockId = "3b9dcab2-028f-4bb8-9c9b-23130d9579ae";

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockRole.getInstitution().getId());

        Mockito.when(roleSavePort.save(Mockito.any(AysRole.class)))
                .thenReturn(Mockito.mock(AysRole.class));

        // Then
        roleUpdateService.delete(mockId);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleSavePort, Mockito.times(1))
                .save(Mockito.any(AysRole.class));

    }

    @Test
    void givenValidId_whenRoleAlreadyDeleted_thenThrowRoleAlreadyDeletedException() {

        // Given
        String mockId = "15c417ce-37cf-4136-b3fb-d9b71c3ce06a";

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withStatus(AysRoleStatus.DELETED)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockRole.getInstitution().getId());

        // Then
        Assertions.assertThrows(
                AysRoleAlreadyDeletedException.class,
                () -> roleUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidId_whenRoleDoesNotFound_thenThrowRoleNotExistByIdException() {

        // Given
        String mockId = "c4dd8650-e616-4aa0-8127-b2944689817d";

        // When
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidId_whenRoleUsing_thenThrowRoleAssignedToUserException() {

        // Given
        String mockId = "ac046642-5e33-49b2-b198-9607625a6ec5";

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();

        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockRole.getInstitution().getId());

        Mockito.when(roleReadPort.isRoleUsing(Mockito.anyString()))
                .thenReturn(true);

        // Then
        Assertions.assertThrows(
                AysRoleAssignedToUserException.class,
                () -> roleUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(roleReadPort, Mockito.times(1))
                .isRoleUsing(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

    @Test
    void givenValidId_whenRoleNotMatchedWithInstitution_thenThrowRoleNotExistByIdException() {

        // Given
        String mockId = "32657ecb-5172-401d-8b29-a8e4454e8243";

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("3b9dcab2-028f-4bb8-9c9b-23130d9579ae");

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleUpdateService.delete(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(roleSavePort, Mockito.never())
                .save(Mockito.any(AysRole.class));
    }

}
