package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.dto.request.AdminUserListRequestBuilder;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

class AdminUserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private AysIdentity identity;


    private static final AdminUserEntityToAdminUserMapper ADMIN_ENTITY_TO_ADMIN_MAPPER = AdminUserEntityToAdminUserMapper.initialize();

    @Test
    void givenUserListRequest_whenAdminwithRoleIsSuperAdmin_thenReturnAllAdminUsers() {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(new AdminUserEntityBuilder().build());
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = ADMIN_ENTITY_TO_ADMIN_MAPPER.map(mockAdminUserEntities);
        AysPage<AdminUser> mockAysPageAdminUsers = AysPage.of(mockPageAdminUserEntities, mockAdminUsers);

        AysUserType userType = AysUserType.SUPER_ADMIN;

        // When
        Mockito.when(identity.getUserType())
                .thenReturn(userType);
        Mockito.when(adminUserRepository.findAll(mockAdminUserListRequest.toPageable()))
                .thenReturn(mockPageAdminUserEntities);

        // Then
        AysPage<AdminUser> aysPageAdminUsers = adminUserService.getAdminUsers(mockAdminUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageAdminUsers, aysPageAdminUsers);

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAll(mockAdminUserListRequest.toPageable());

    }

    @Test
    void givenUserListRequest_whenAdminwithRoleIsAdmin_thenReturnAllAdminUsers() {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().withValidFields().build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(mockAdminUserEntity);
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = ADMIN_ENTITY_TO_ADMIN_MAPPER.map(mockAdminUserEntities);
        AysPage<AdminUser> mockAysPageAdminUsers = AysPage.of(mockPageAdminUserEntities, mockAdminUsers);

        AysUserType userType = AysUserType.ADMIN;

        // When
        Mockito.when(identity.getUserType()).thenReturn(userType);
        Mockito.when(identity.getOrganizationId()).thenReturn(mockAdminUserEntity.getOrganizationId());

        Mockito.when(adminUserRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageAdminUserEntities);

        AysPage<AdminUser> aysPageAdminUsers = adminUserService.getAdminUsers(mockAdminUserListRequest);

        // Then
        AysPageBuilder.assertEquals(mockAysPageAdminUsers, aysPageAdminUsers);

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

    }

}