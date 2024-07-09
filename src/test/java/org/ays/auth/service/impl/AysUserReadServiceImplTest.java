package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.util.AysRandomUtil;
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
    void givenValidUserId_whenUserFoundById_thenReturnUser() {

        // Given
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
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
        Mockito.verify(userReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidUserId_whenUserNotFoundById_thenThrowAysUserNotExistByIdException() {

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

}
