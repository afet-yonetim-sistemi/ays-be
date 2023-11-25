package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class AdminUserRegisterApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserRegisterApplicationService adminUserRegisterApplicationService;


    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admin";


    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFound_thenReturnAdminUserRegisterApplicationResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AdminUserRegisterApplication mockData = new AdminUserRegisterApplicationBuilder()
                .withId(mockId)
                .build();
        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplicationById(mockId))
                .thenReturn(mockData);


        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId);
        AdminUserRegisterApplicationResponse mockResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.map(mockData);
        AysResponse<AdminUserRegisterApplicationResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockSuperAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .getRegistrationApplicationById(mockId);

    }


    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }
}
