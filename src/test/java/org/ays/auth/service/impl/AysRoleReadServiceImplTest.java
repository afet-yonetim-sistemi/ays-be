package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysRoleFilter;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.request.AysRoleListRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.util.exception.AysRoleNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class AysRoleReadServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysRoleReadServiceImpl roleReadService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysIdentity identity;


    @Test
    void whenRolesFound_thenReturnRoles() {

        // When
        List<AysRole> mockRoles = List.of(
                new AysRoleBuilder().withValidValues().build(),
                new AysRoleBuilder().withValidValues().build(),
                new AysRoleBuilder().withValidValues().build(),
                new AysRoleBuilder().withValidValues().build(),
                new AysRoleBuilder().withValidValues().build()
        );
        Mockito.when(roleReadPort.findAll())
                .thenReturn(mockRoles);

        // Then
        List<AysRole> roles = roleReadService.findAll();

        Assertions.assertEquals(mockRoles.size(), roles.size());

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAll();
    }


    @Test
    void givenValidListRequest_whenRolesFound_thenReturnAysPageOfRoles() {

        // Given
        AysRoleListRequest mockListRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .build();

        // When
        AysPageable aysPageable = mockListRequest.getPageable();
        AysRoleFilter filter = mockListRequest.getFilter();

        List<AysRole> mockRoles = List.of(
                new AysRoleBuilder()
                        .withValidValues()
                        .build()
        );
        AysPage<AysRole> mockRolesPage = AysPageBuilder.from(mockRoles, aysPageable);

        Mockito.when(roleReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any(AysRoleFilter.class)))
                .thenReturn(mockRolesPage);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        AysPage<AysRole> rolesPage = roleReadService.findAll(mockListRequest);

        AysPageBuilder.assertEquals(mockRolesPage, rolesPage);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidListRequest_whenRolesNotFound_thenReturnAysPageOfRoles() {

        // Given
        AysRoleListRequest mockListRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .build();

        // When
        AysPageable aysPageable = mockListRequest.getPageable();
        AysRoleFilter filter = mockListRequest.getFilter();

        List<AysRole> mockRoles = List.of();
        AysPage<AysRole> mockRolesPage = AysPageBuilder.from(mockRoles, aysPageable);

        Mockito.when(roleReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any(AysRoleFilter.class)))
                .thenReturn(mockRolesPage);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        AysPage<AysRole> rolesPage = roleReadService.findAll(mockListRequest);

        AysPageBuilder.assertEquals(mockRolesPage, rolesPage);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }


    @Test
    void givenValidRoleId_whenRoleFoundById_thenReturnRole() {

        // Given
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withInstitution(mockInstitution)
                .build();
        String mockId = mockRole.getId();

        // When
        Mockito.when(roleReadPort.findById(mockId))
                .thenReturn(Optional.of(mockRole));

        // Then
        AysRole result = roleReadService.findById(mockId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockRole, result);

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidRoleId_whenRoleNotFoundById_thenThrowAysRoleNotExistException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(roleReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleReadService.findById(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidRoleId_whenRoleNotFoundForUser_thenThrowAysRoleNotExistException() {

        // Given
        String mockId = "7a801611-735f-42b1-833a-0e9a9e5ed9d2";

        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleReadPort.findById(mockId))
                .thenReturn(Optional.of(mockRole));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("c009f6f9-1005-490b-9210-db64b6f61c37");

        // Then
        Assertions.assertThrows(
                AysRoleNotExistByIdException.class,
                () -> roleReadService.findById(mockId)
        );

        // Verify
        Mockito.verify(roleReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

}
