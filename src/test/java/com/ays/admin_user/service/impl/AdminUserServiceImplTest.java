package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminEntityToAdminUserMapper;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserListRequestBuilder;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdminUserServiceImplTest extends AbstractUnitTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private static final AdminEntityToAdminUserMapper ADMIN_ENTITY_TO_ADMIN_MAPPER = AdminEntityToAdminUserMapper.initialize();

    @Test
    void givenUserListRequest_whenAdminwithRoleIsSuperAdmin_thenReturnAllAdminUsers() {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(new AdminUserEntityBuilder().build());
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = ADMIN_ENTITY_TO_ADMIN_MAPPER.map(mockAdminUserEntities);
        AysPage<AdminUser> mockAysPageAdminUsers = AysPage.of(mockPageAdminUserEntities, mockAdminUsers);

        // Set up authentication
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("SUPER_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        Mockito.when(adminUserRepository.findAll(mockUserListRequest.toPageable()))
                .thenReturn(mockPageAdminUserEntities);

        // Then
        AysPage<AdminUser> aysPageAdminUsers = adminUserService.getAllAdminUsers(mockUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageAdminUsers, aysPageAdminUsers);

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAll(mockUserListRequest.toPageable());
    }

    @Test
    void givenUserListRequest_whenAdminwithRoleIsAdmin_thenReturnAllAdminUsers() {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        String username = "admin";
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withUsername(username)
                .withOrganizationId("org-123")
                .build();

        List<AdminUserEntity> mockAdminUserEntities = Collections.singletonList(mockAdminUserEntity);
        Page<AdminUserEntity> mockPageAdminUserEntities = new PageImpl<>(mockAdminUserEntities);

        List<AdminUser> mockAdminUsers = ADMIN_ENTITY_TO_ADMIN_MAPPER.map(mockAdminUserEntities);
        AysPage<AdminUser> mockAysPageAdminUsers = AysPage.of(mockPageAdminUserEntities, mockAdminUsers);

        // Set up authentication
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(adminUserRepository.findByUsername(username)).thenReturn(Optional.of(mockAdminUserEntity));
        Mockito.when(adminUserRepository.findAllByOrganizationId(mockAdminUserEntity.getOrganizationId(), mockUserListRequest.toPageable()))
                .thenReturn(mockPageAdminUserEntities);

        // When
        AysPage<AdminUser> aysPageAdminUsers = adminUserService.getAllAdminUsers(mockUserListRequest);

        // Then
        AysPageBuilder.assertEquals(mockAysPageAdminUsers, aysPageAdminUsers);

        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findByUsername(username);
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .findAllByOrganizationId(mockAdminUserEntity.getOrganizationId(), mockUserListRequest.toPageable());
    }
}