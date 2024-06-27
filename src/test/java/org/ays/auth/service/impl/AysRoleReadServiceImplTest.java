package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysRoleFilter;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.request.AysRoleListRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

class AysRoleReadServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysRoleReadServiceImpl roleReadService;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysIdentity identity;


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


}
