package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.repository.specification.AdminUserSpecifications;
import com.ays.admin_user.service.AdminUserService;
import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.AysPage;
import com.ays.common.util.exception.AysUnexpectedArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class implements the {@link AdminUserService} interface and provides methods for
 * performing admin operations. It uses the {@link AdminUserRepository} and the
 * {@link AdminUserEntityToAdminUserMapper} for mapping the request to entity objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;

    private final AdminUserEntityToAdminUserMapper adminEntityToAdminMapper = AdminUserEntityToAdminUserMapper.initialize();

    private final AysIdentity aysIdentity;

    /**
     * Get All admin users based on the given {@link AdminUserListRequest} object. First, it is determined whether
     * ADMIN or SUPER_ADMIN in terms of admin role. Next, it returns the list of all admins in all organizations
     * for SUPER_ADMIN role or return the list of all admins in the same organization for ADMIN role.
     *
     * @param listRequest the request object covering page and pageSize
     * @return admin user list
     * @throws AccessDeniedException if an admin user role cannot be accessed
     */
    @Override
    public AysPage<AdminUser> getAdminUsers(AdminUserListRequest listRequest) {
        AysUserType aysUserType = aysIdentity.getUserType();

        return switch (aysUserType) {
            case SUPER_ADMIN -> getAdminUsersWithSuperAdminUsers(listRequest);
            case ADMIN -> getAdminUsersWithAdmin(listRequest);
            default -> throw new AysUnexpectedArgumentException("Invalid user type: " + aysUserType.name());
        };
    }


    /**
     * Handle Super Admin method is used for getting all super admins of all organizations
     *
     * @param listRequest the request object covering page and pageSize
     * @return super admin user list
     */
    private AysPage<AdminUser> getAdminUsersWithSuperAdminUsers(AdminUserListRequest listRequest) {
        Page<AdminUserEntity> adminUserEntities = adminUserRepository.findAll(listRequest.toPageable());
        List<AdminUser> adminUsers = adminEntityToAdminMapper.map(adminUserEntities.getContent());
        return AysPage.of(adminUserEntities, adminUsers);
    }

    /**
     * Handle Admin method is used for getting all admins with the same organization
     *
     * @param listRequest the request object covering page and pageSize
     * @return super admin user list
     */
    private AysPage<AdminUser> getAdminUsersWithAdmin(AdminUserListRequest listRequest) {
        String organizationId = aysIdentity.getOrganizationId();
        Specification<AdminUserEntity> specification = Specification
                .where(AdminUserSpecifications.hasOrganizationId(organizationId));
        Page<AdminUserEntity> adminUserEntitiesByOrganization = adminUserRepository.findAll(specification, listRequest.toPageable());
        List<AdminUser> adminUsersByOrganization = adminEntityToAdminMapper.map(adminUserEntitiesByOrganization.getContent());
        return AysPage.of(adminUserEntitiesByOrganization, adminUsersByOrganization);
    }
}
