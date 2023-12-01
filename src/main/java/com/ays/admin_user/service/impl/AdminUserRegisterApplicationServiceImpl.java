package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.AysPage;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class implements the {@link AdminUserRegisterApplicationService} interface and provides verification operations for admin users.
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 */
@Service
@RequiredArgsConstructor
public class AdminUserRegisterApplicationServiceImpl implements AdminUserRegisterApplicationService {

    private final AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;


    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();


    /**
     * Retrieves a paginated list of admin user registration applications based on the specified filters
     *
     * @param listRequest covering page, pageSize and filters
     * @return a paginated list of admin user registration applications
     */
    @Override
    public AysPage<AdminUserRegisterApplication> getRegistrationApplications(AdminUserRegisterApplicationListRequest listRequest) {
        final Specification<AdminUserRegisterApplicationEntity> filters = listRequest
                .toSpecification(AdminUserRegisterApplicationEntity.class);

        final Page<AdminUserRegisterApplicationEntity> registerApplicationEntities = adminUserRegisterApplicationRepository
                .findAll(filters, listRequest.toPageable());

        final List<AdminUserRegisterApplication> registerApplications = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntities.getContent());

        return AysPage.of(
                listRequest.getFilter(),
                registerApplicationEntities,
                registerApplications
        );
    }

    /**
     * Retrieves an admin user register application by id.
     *
     * @param id The id of the register application.
     * @return An admin user register application.
     */
    @Override
    public AdminUserRegisterApplication getRegistrationApplicationById(String id) {
        final AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationNotExistByIdException(id));

        return adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntity);
    }

}
