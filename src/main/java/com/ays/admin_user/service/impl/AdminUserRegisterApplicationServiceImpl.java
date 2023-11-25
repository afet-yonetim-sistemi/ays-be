package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
