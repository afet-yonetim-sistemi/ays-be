package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AdminUserRegisterApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserRegisterServiceImpl adminUserRegisterService;

    @Mock
    private AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;
}
