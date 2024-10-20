package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysUserNotExistByIdException;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.port.AysUserReadPort;
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

class AysUserReadServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserReadServiceImpl userReadService;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysIdentity identity;


    @Test
    void givenValidListRequest_whenUsersFound_thenReturnAysPageOfUsers() {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .build();

        // When
        AysPageable aysPageable = mockListRequest.getPageable();
        AysUserFilter filter = mockListRequest.getFilter();

        List<AysUser> mockUsers = List.of(
                new AysUserBuilder()
                        .withValidValues()
                        .build()
        );
        AysPage<AysUser> mockUsersPage = AysPageBuilder.from(mockUsers, aysPageable);

        Mockito.when(userReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any(AysUserFilter.class)))
                .thenReturn(mockUsersPage);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        AysPage<AysUser> usersPage = userReadService.findAll(mockListRequest);

        AysPageBuilder.assertEquals(mockUsersPage, usersPage);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidListRequest_whenUsersNotFound_thenReturnAysPageOfUsers() {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .build();

        // When
        AysPageable aysPageable = mockListRequest.getPageable();
        AysUserFilter filter = mockListRequest.getFilter();

        List<AysUser> mockUsers = List.of();
        AysPage<AysUser> mockUsersPage = AysPageBuilder.from(mockUsers, aysPageable);

        Mockito.when(userReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any(AysUserFilter.class)))
                .thenReturn(mockUsersPage);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        AysPage<AysUser> usersPage = userReadService.findAll(mockListRequest);

        AysPageBuilder.assertEquals(mockUsersPage, usersPage);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }


    @Test
    void givenValidId_whenUserFoundById_thenReturnUser() {

        // Given
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .build();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitution.getId());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withInstitution(mockInstitution)
                .build();
        String mockId = mockUser.getId();

        // When
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        // Then
        AysUser result = userReadService.findById(mockId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockUser, result);

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidId_whenUserNotFoundById_thenThrowAysUserNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userReadService.findById(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidId_whenUserNotFoundForUser_thenThrowUserNotExistException() {

        // Given
        String mockId = "71520700-d5fb-4c7d-8b14-9088136a9cd5";

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(userReadPort.findById(mockId))
                .thenReturn(Optional.of(mockUser));

        Mockito.when(identity.getInstitutionId())
                .thenReturn("394d98c1-a1d0-4c1c-8b39-28b913a0abbf");

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userReadService.findById(mockId)
        );
    }

}
