package org.ays.admin_user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.AdminUser;
import org.ays.admin_user.model.dto.request.AdminUserListRequest;
import org.ays.admin_user.model.dto.request.AdminUserListRequestBuilder;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.enums.AysUserType;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
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

@Deprecated(since = "AdminUserServiceImplTest V2 Production'a alınınca burası silinecektir.", forRemoval = true)
class AdminUserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private AysIdentity identity;


    private final AdminUserEntityToAdminUserMapper adminUserEntityToAdminUserMapper = AdminUserEntityToAdminUserMapper.initialize();


    @Test
    void givenUserListRequest_whenAdminWithRoleIsSuperAdmin_thenReturnAllAdminUsers() {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(new AdminUserEntityBuilder().build());
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = adminUserEntityToAdminUserMapper.map(mockAdminUserEntities);
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

        // Verify
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAll(mockAdminUserListRequest.toPageable());
    }

    @Test
    void givenUserListRequest_whenAdminWithRoleIsAdmin_thenReturnAllAdminUsers() {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder().withValidFields().build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(mockAdminUserEntity);
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = adminUserEntityToAdminUserMapper.map(mockAdminUserEntities);
        AysPage<AdminUser> mockAysPageAdminUsers = AysPage.of(mockPageAdminUserEntities, mockAdminUsers);

        AysUserType userType = AysUserType.ADMIN;

        // When
        Mockito.when(identity.getUserType()).thenReturn(userType);
        Mockito.when(identity.getInstitutionId()).thenReturn(mockAdminUserEntity.getInstitutionId());

        Mockito.when(adminUserRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageAdminUserEntities);

        // Then
        AysPage<AdminUser> aysPageAdminUsers = adminUserService.getAdminUsers(mockAdminUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageAdminUsers, aysPageAdminUsers);

        // Verify
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

}
