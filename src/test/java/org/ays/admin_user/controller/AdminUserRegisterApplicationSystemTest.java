package org.ays.admin_user.controller;

import org.ays.AbstractSystemTest;
import org.ays.admin_user.model.AdminUserRegisterApplication;
import org.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysError;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
        AysResponse<AysPageResponse<AdminUserRegisterApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
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

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
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
        AysResponse<AdminUserRegisterApplicationResponse> mockResponse = AysResponse
                .successOf(adminUserRegisterApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("adminUser.phoneNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("adminUser.phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("adminUser.phoneNumber.lineNumber")
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(applicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
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
        AysResponse<AdminUserRegisterApplicationCreateResponse> mockResponse = AysResponse
                .successOf(adminUserRegisterApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
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

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminUserRegisterApplication adminUserRegisterApplication = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper
                .map(adminUserRegisterApplicationEntity);
        AdminUserRegisterApplicationSummaryResponse adminUserRegisterApplicationSummaryResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper
                .map(adminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationSummaryResponse> mockResponse = AysResponse.successOf(adminUserRegisterApplicationSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
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

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
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

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
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

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
