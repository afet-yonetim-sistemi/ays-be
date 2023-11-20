package com.ays.admin_user.service.impl;

import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
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

}
