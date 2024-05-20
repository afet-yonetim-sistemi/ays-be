package org.ays.admin_user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.admin_user.model.AdminUser;
import org.ays.admin_user.model.dto.request.AdminUserListRequest;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.admin_user.service.AdminUserService;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.enums.AysUserType;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysSpecification;
import org.ays.common.util.exception.AysUnexpectedArgumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * This service class implements the {@link AdminUserService} interface and provides methods for
 * performing admin operations. It uses the {@link AdminUserRepository} and the
 * {@link AdminUserEntityToAdminUserMapper} for mapping the request to entity objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Deprecated(since = "AdminUserServiceImpl V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;

    private static final AdminUserEntityToAdminUserMapper adminEntityToAdminMapper = AdminUserEntityToAdminUserMapper.initialize();

    private final AysIdentity identity;

    /**
     * Get Admin users based on the given {@link AdminUserListRequest} object. First, it is determined whether
     * ADMIN or SUPER_ADMIN in terms of admin role. Next, it returns the list of all admins in all institutions
     * for SUPER_ADMIN role or return the list of all admins in the same institution for ADMIN role.
     *
     * @param listRequest the request object covering page and pageSize
     * @return admin list
     * @throws AccessDeniedException if an admin role cannot be accessed
     */
    @Override
    public AysPage<AdminUser> getAdminUsers(AdminUserListRequest listRequest) {
        AysUserType aysUserType = identity.getUserType();

        return switch (aysUserType) {
            case SUPER_ADMIN -> this.getAdminAndSuperAdminUsersFromDatabase(listRequest);
            case ADMIN -> this.getAdminUsersFromDatabase(listRequest);
            default -> throw new AysUnexpectedArgumentException(aysUserType.name());
        };
    }

    /**
     * Handle Super Admin method is used for getting all super admins of all institutions
     *
     * @param listRequest the request object covering page and pageSize
     * @return super admin list
     */
    private AysPage<AdminUser> getAdminAndSuperAdminUsersFromDatabase(AdminUserListRequest listRequest) {
        Page<AdminUserEntity> adminUserEntities = adminUserRepository.findAll(listRequest.toPageable());
        List<AdminUser> adminUsers = adminEntityToAdminMapper.map(adminUserEntities.getContent());
        return AysPage.of(adminUserEntities, adminUsers);
    }

    /**
     * Handle Admin method is used for getting all admins with the same institution
     *
     * @param listRequest the request object covering page and pageSize
     * @return super admin list
     */
    private AysPage<AdminUser> getAdminUsersFromDatabase(final AdminUserListRequest listRequest) {

        final Map<String, Object> filter = Map.of("institutionId", identity.getInstitutionId());
        final Specification<AdminUserEntity> specification = AysSpecification.<AdminUserEntity>builder().and(filter);

        final Page<AdminUserEntity> adminUserEntitiesByInstitution = adminUserRepository
                .findAll(specification, listRequest.toPageable());

        final List<AdminUser> adminUsersByInstitution = adminEntityToAdminMapper
                .map(adminUserEntitiesByInstitution.getContent());
        return AysPage.of(adminUserEntitiesByInstitution, adminUsersByInstitution);
    }
}
