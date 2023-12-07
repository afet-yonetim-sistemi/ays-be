package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class AdminUserRegisterApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserRegisterApplicationService adminUserRegisterApplicationService;


    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/admin";


    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationsFound_thenReturnAysPageResponseOfAdminUserRegisterApplicationsResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationListRequest mockListRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues().build();

        // When
        List<AdminUserRegisterApplicationEntity> mockEntities = List.of(new AdminUserRegisterApplicationEntityBuilder().withValidFields().build());
        Page<AdminUserRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);
        List<AdminUserRegisterApplication> mockList = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminUserRegisterApplication> mockAysPage = AysPage
                .of(mockListRequest.getFilter(), mockPageEntities, mockList);

        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplications(Mockito.any(AdminUserRegisterApplicationListRequest.class)))
                .thenReturn(mockAysPage);

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        List<AdminUserRegisterApplicationsResponse> mockResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.map(mockList);
        AysPageResponse<AdminUserRegisterApplicationsResponse> pageOfResponse = AysPageResponse.<AdminUserRegisterApplicationsResponse>builder()
                .of(mockAysPage)
                .content(mockResponse)
                .build();

        AysResponse<AysPageResponse<AdminUserRegisterApplicationsResponse>> mockAysResponse = AysResponse.successOf(pageOfResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().isNotEmpty());

        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .getRegistrationApplications(Mockito.any(AdminUserRegisterApplicationListRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminUserRegisterApplicationListRequest mockListRequest = new AdminUserRegisterApplicationListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken(), mockListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

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

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserApplicationFound_thenReturnAdminUserApplicationSummaryResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplication mockAdminUserRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withId(mockId)
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplicationSummaryById(mockId))
                .thenReturn(mockAdminUserRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId).concat("/summary"));
        AdminUserRegisterApplicationSummaryResponse mockResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.map(mockAdminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationSummaryResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint))
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
    }

}
