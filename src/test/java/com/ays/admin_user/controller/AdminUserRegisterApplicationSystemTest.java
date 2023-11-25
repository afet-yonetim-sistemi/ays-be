package com.ays.admin_user.controller;

import com.ays.AbstractSystemTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class AdminUserRegisterApplicationSystemTest extends AbstractSystemTest {

    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admin";


    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationsFound_thenReturnAdminUserRegisterApplicationsResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationListRequest listRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues().build();

        List<AdminUserRegisterApplicationEntity> entities = List.of(new AdminUserRegisterApplicationEntityBuilder().withValidFields().build());
        Page<AdminUserRegisterApplicationEntity> pageOfEntities = new PageImpl<>(entities);
        List<AdminUserRegisterApplication> listData = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(entities);
        AysPage<AdminUserRegisterApplication> aysPage = AysPage.of(pageOfEntities, listData);
        AysPageResponse<AdminUserRegisterApplication> aysPageResponse = AysPageResponse.<AdminUserRegisterApplication>builder()
                .of(aysPage).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        AysResponse<AysPageResponse<AdminUserRegisterApplication>> response = AysResponse.successOf(aysPageResponse);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminUserRegisterApplicationListRequest listRequest = new AdminUserRegisterApplicationListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), listRequest);

        AysResponse<AysError> response = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
