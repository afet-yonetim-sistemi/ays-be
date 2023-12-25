package com.ays.admin_user.controller;

import com.ays.AbstractSystemTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequestBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class AdminUserRegisterApplicationSystemTest extends AbstractSystemTest {

    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admin";


    private void initialize(AdminUserRegisterApplicationEntity mockEntity) {
        adminUserRegisterApplicationRepository.save(mockEntity);
    }

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

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // When
        AdminUserRegisterApplication mockData = new AdminUserRegisterApplicationBuilder()
                .withId(AysRandomUtil.generateUUID())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), mockRequest);


        AdminUserRegisterApplicationCreateResponse mockResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper.map(mockData);
        AysResponse<AdminUserRegisterApplicationCreateResponse> mockAysResponse = AysResponse.successOf(mockResponse);
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
    void givenValidAdminUserRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), mockRequest);

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

        // Initialize
        AdminUserEntity mockAdminUserEntity = adminUserRepository.findById(AysValidTestData.AdminUser.ID)
                .orElseThrow();
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

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId).concat("/summary"));
        AdminUserRegisterApplication mockAdminUserRegisterApplication = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntity);
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

    @Test
    void givenValidAdminUserRegisterRequest_whenAdminUserRegistered_thenReturnSuccessResponse() throws Exception {
        // Given
        String applicationId = AysValidTestData.APPLICATION_ID;
        AdminUserRegisterApplicationCompleteRequest registerRequest = new AdminUserRegisterRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");
        AysResponse<Void> response = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, registerRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String applicationId = AysValidTestData.APPLICATION_ID;
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminUserRegisterApplicationCompleteRequest registerRequest = new AdminUserRegisterRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, registerRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(errorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(errorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(errorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String applicationId = AysValidTestData.APPLICATION_ID;
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminUserRegisterApplicationCompleteRequest registerRequest = new AdminUserRegisterRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, registerRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(errorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(errorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(errorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String applicationId = AysValidTestData.APPLICATION_ID;
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminUserRegisterApplicationCompleteRequest registerRequest = new AdminUserRegisterRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, registerRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(errorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(errorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(errorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }
}
