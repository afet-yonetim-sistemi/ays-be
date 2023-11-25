package com.ays.admin_user.controller;

import com.ays.AbstractSystemTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class AdminUserRegisterApplicationSystemTest extends AbstractSystemTest {

    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admin";


    private void initialize(AdminUserRegisterApplicationEntity mockEntity) {
        adminUserRegisterApplicationRepository.save(mockEntity);
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFound_thenReturnAdminUserRegisterApplicationResponse() throws Exception {

        // Initialize
        AdminUserEntity mockAdminUserEntity = adminUserRepository.findById(AysValidTestData.AdminUser.ID).get();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(mockAdminUserEntity.getId())
                .withAdminUser(mockAdminUserEntity)
                .withInstitutionId(mockAdminUserEntity.getInstitutionId())
                .withInstitution(mockAdminUserEntity.getInstitution())
                .build();
        this.initialize(mockEntity);

        // Given
        String mockId = mockEntity.getId();

        // When
        AdminUserRegisterApplication mockData = new AdminUserRegisterApplicationBuilder()
                .withId(mockId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AdminUserRegisterApplicationResponse mockResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.map(mockData);
        AysResponse<AdminUserRegisterApplicationResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .exists());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

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
