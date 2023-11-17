package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import org.springframework.boot.test.mock.mockito.MockBean;

class AdminUserRegisterApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserRegisterApplicationService adminUserRegisterApplicationService;
}
