package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.mapper.AdminEntityToAdminUserMapper;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.service.AdminUserService;
import com.ays.admin_user.util.exception.AysAdminUserNotFoundException;
import com.ays.common.model.AysPage;
import com.ays.user.model.dto.request.UserListRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class implements the {@link AdminUserService} interface and provides methods for
 * performing admin operations. It uses the {@link AdminUserRepository} and the
 * {@link AdminEntityToAdminUserMapper} for mapping the request to entity objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;

    private final AdminEntityToAdminUserMapper adminEntityToAdminMapper = AdminEntityToAdminUserMapper.initialize();

    /**
     * Get All admin users based on the given {@link UserListRequest} object. First, it is determined whether
     * ADMIN or SUPER_ADMIN in terms of admin role. Next, it returns the list of all admins in all organizations
     * for SUPER_ADMIN role or return the list of all admins in the same organization for ADMIN role.
     *
     * @param listRequest the request object covering page and pageSize
     * @return admin user list
     * @throws AccessDeniedException if an admin user role cannot be accessed
     */
    @Override
    public AysPage<AdminUser> getAllAdminUsers(UserListRequest listRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (authorities.contains("SUPER_ADMIN")) {
            Page<AdminUserEntity> adminUserEntities = adminUserRepository.findAll(listRequest.toPageable());
            List<AdminUser> adminUsers = adminEntityToAdminMapper.map(adminUserEntities.getContent());
            return AysPage.of(
                    adminUserEntities,
                    adminUsers
            );
        } else if (authorities.contains("ADMIN")) {
            String username = authentication.getName();
            AdminUserEntity adminUserEntity = adminUserRepository.findByUsername(username).orElseThrow(() -> new AysAdminUserNotFoundException(username));
            Page<AdminUserEntity> adminUserEntities = adminUserRepository.findAllByOrganizationId(adminUserEntity.getOrganizationId(), listRequest.toPageable());
            List<AdminUser> adminUsers = adminEntityToAdminMapper.map(adminUserEntities.getContent());
            return AysPage.of(adminUserEntities, adminUsers);
        } else {
            throw new AccessDeniedException("User does not have the required authority to access admin users.");
        }

    }
}
