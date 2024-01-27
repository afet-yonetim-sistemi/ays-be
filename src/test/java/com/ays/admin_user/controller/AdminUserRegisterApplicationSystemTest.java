package com.ays.admin_user.controller;

import com.ays.AbstractSystemTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequestBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequestBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.admin_user.model.enums.AdminUserStatus;
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
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
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
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper.initialize();


    private void initialize(InstitutionEntity institutionEntity) {
        institutionRepository.save(institutionEntity);
    }

    private void initialize(InstitutionEntity institutionEntity,
                            AdminUserEntity adminUserEntity,
                            AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity) {

        institutionRepository.save(institutionEntity);
        adminUserRepository.save(adminUserEntity);
        adminUserRegisterApplicationRepository.save(adminUserRegisterApplicationEntity);
    }


    private static final String BASE_PATH = "/api/v1/admin";

    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationsFound_thenReturnAdminUserRegisterApplicationsResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationListRequest listRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        List<AdminUserRegisterApplicationEntity> adminUserRegisterApplicationEntities = List.of(
                new AdminUserRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminUserRegisterApplicationEntity> pageOfEntities = new PageImpl<>(
                adminUserRegisterApplicationEntities
        );
        List<AdminUserRegisterApplication> adminUserRegisterApplications = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper
                .map(adminUserRegisterApplicationEntities);
        AysPage<AdminUserRegisterApplication> page = AysPage.of(
                pageOfEntities,
                adminUserRegisterApplications
        );
        AysPageResponse<AdminUserRegisterApplication> pageResponse = AysPageResponse.<AdminUserRegisterApplication>builder()
                .of(page).build();
        AysResponse<AysPageResponse<AdminUserRegisterApplication>> response = AysResponse.successOf(pageResponse);

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
        AdminUserRegisterApplicationListRequest adminUserRegisterApplicationListRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), adminUserRegisterApplicationListRequest);

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
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();

        // When
        AdminUserRegisterApplication adminUserRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withId(applicationId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AdminUserRegisterApplicationResponse adminUserRegisterApplicationResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper
                .map(adminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationResponse> response = AysResponse.successOf(adminUserRegisterApplicationResponse);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .exists());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(applicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

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
    void givenValidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        this.initialize(institutionEntity);

        // Given
        AdminUserRegisterApplicationCreateRequest createRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), createRequest);

        AdminUserRegisterApplication adminUserRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withId(AysRandomUtil.generateUUID())
                .build();
        AdminUserRegisterApplicationCreateResponse adminUserRegisterApplicationCreateResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper
                .map(adminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationCreateResponse> response = AysResponse.successOf(adminUserRegisterApplicationCreateResponse);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .exists());
    }

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        this.initialize(institutionEntity);

        // Given
        AdminUserRegisterApplicationCreateRequest createRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), createRequest);

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
    void givenValidAdminUserRegisterApplicationId_whenAdminUserApplicationFound_thenReturnAdminUserApplicationSummaryResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(applicationId).concat("/summary"));

        AdminUserRegisterApplication adminUserRegisterApplication = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper
                .map(adminUserRegisterApplicationEntity);
        AdminUserRegisterApplicationSummaryResponse adminUserRegisterApplicationSummaryResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper
                .map(adminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationSummaryResponse> response = AysResponse.successOf(adminUserRegisterApplicationSummaryResponse);

        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminUserRegisterRequest_whenAdminUserRegistered_thenReturnSuccessResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();
        AdminUserRegisterApplicationCompleteRequest completeRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");

        AysResponse<Void> response = AysResponseBuilder.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, completeRequest))
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

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminUserRegisterApplicationCompleteRequest completeRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");

        AysError response = AysErrorBuilder.VALIDATION_ERROR;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, completeRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(response.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminUserRegisterApplicationCompleteRequest completeRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");

        AysError response = AysErrorBuilder.VALIDATION_ERROR;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, completeRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(response.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminUserRegisterApplicationCompleteRequest completeRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/complete");

        AysError response = AysErrorBuilder.VALIDATION_ERROR;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, completeRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(response.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenApproveAdminUserRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserStatus.NOT_VERIFIED)
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(applicationId).concat("/approve"));

        AysResponse<Void> response = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, superAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(true))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForApprovingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken());

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
    void givenValidAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        AdminUserEntity adminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserStatus.NOT_VERIFIED)
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(null)
                .build();
        AdminUserRegisterApplicationEntity adminUserRegisterApplicationEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                .withAdminUserId(adminUserEntity.getId())
                .withAdminUser(null)
                .withInstitutionId(adminUserEntity.getInstitutionId())
                .withInstitution(null)
                .build();
        this.initialize(institutionEntity, adminUserEntity, adminUserRegisterApplicationEntity);

        // Given
        String applicationId = adminUserRegisterApplicationEntity.getId();
        AdminUserRegisterApplicationRejectRequest rejectRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), rejectRequest);

        AysResponse<Void> response = AysResponse.SUCCESS;

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminUserRegisterApplicationRejectRequest_whenUnauthorizedForRejectingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationRejectRequest rejectRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), rejectRequest);

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
