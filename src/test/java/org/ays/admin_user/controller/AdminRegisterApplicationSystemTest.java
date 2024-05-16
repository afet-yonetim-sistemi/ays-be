package org.ays.admin_user.controller;

import org.ays.AbstractSystemTest;
import org.ays.admin_user.model.AdminRegisterApplicationBuilder;
import org.ays.admin_user.model.AdminRegistrationApplication;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationCreateResponse;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationResponse;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationSummaryResponse;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationEntityToAdminRegisterApplicationMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationResponseMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.RoleEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.enums.UserStatus;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AdminRegisterApplicationSystemTest extends AbstractSystemTest {

    private final AdminRegisterApplicationEntityToAdminRegisterApplicationMapper adminRegisterApplicationEntityToAdminRegisterApplicationMapper = AdminRegisterApplicationEntityToAdminRegisterApplicationMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationResponseMapper adminRegisterApplicationToAdminRegisterApplicationResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationResponseMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper adminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper adminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper.initialize();


    private void initialize(InstitutionEntity institutionEntity) {
        institutionRepository.save(institutionEntity);
    }

    private void initialize(InstitutionEntity institutionEntity,
                            RoleEntity roleEntity,
                            UserEntityV2 userEntity,
                            AdminRegisterApplicationEntity adminRegisterApplicationEntity) {

        institutionRepository.save(institutionEntity);
        roleRepository.save(roleEntity);
        userRepositoryV2.save(userEntity);
        adminRegisterApplicationRepository.save(adminRegisterApplicationEntity);
    }


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidAdminRegisterApplicationListRequest_whenAdminRegisterApplicationsFound_thenReturnAdminRegisterApplicationsResponse() throws Exception {

        // Given
        AdminRegisterApplicationListRequest listRequest = new AdminRegisterApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        List<AdminRegisterApplicationEntity> adminRegisterApplicationEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().withStatus(AdminRegistrationApplicationStatus.WAITING).build()
        );
        Page<AdminRegisterApplicationEntity> pageOfEntities = new PageImpl<>(
                adminRegisterApplicationEntities
        );
        List<AdminRegistrationApplication> adminRegistrationApplications = adminRegisterApplicationEntityToAdminRegisterApplicationMapper
                .map(adminRegisterApplicationEntities);
        AysPage<AdminRegistrationApplication> page = AysPage.of(
                pageOfEntities,
                adminRegistrationApplications
        );
        AysPageResponse<AdminRegistrationApplication> pageResponse = AysPageResponse.<AdminRegistrationApplication>builder()
                .of(page).build();
        AysResponse<AysPageResponse<AdminRegistrationApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("reason")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("institution")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("institution.id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("institution.name")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.id")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.firstName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.lastName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.city")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.emailAddress")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.phoneNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.phoneNumber.countryCode")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("user.phoneNumber.lineNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegisterApplicationListRequest_whenUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminRegisterApplicationListRequest adminRegisterApplicationListRequest = new AdminRegisterApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), adminRegisterApplicationListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenAdminRegisterApplicationFound_thenReturnAdminRegisterApplicationResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();

        // When
        AdminRegistrationApplication adminRegistrationApplication = adminRegisterApplicationEntityToAdminRegisterApplicationMapper
                .map(adminRegisterApplicationEntity);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AdminRegisterApplicationResponse adminRegisterApplicationResponse = adminRegisterApplicationToAdminRegisterApplicationResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegisterApplicationResponse> mockResponse = AysResponse
                .successOf(adminRegisterApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("reason")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("institution")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("institution.id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("institution.name")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.firstName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.lastName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.city")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.emailAddress")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.phoneNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("user.phoneNumber.lineNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenUnauthorizedForGettingAdminRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnAdminRegisterApplicationCreateResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        this.initialize(institutionEntity);

        // Given
        AdminRegisterApplicationCreateRequest createRequest = new AdminRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), createRequest);

        AdminRegistrationApplication adminRegistrationApplication = new AdminRegisterApplicationBuilder()
                .withId(AysRandomUtil.generateUUID())
                .build();
        AdminRegisterApplicationCreateResponse adminRegisterApplicationCreateResponse = adminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegisterApplicationCreateResponse> mockResponse = AysResponse
                .successOf(adminRegisterApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        this.initialize(institutionEntity);

        // Given
        AdminRegisterApplicationCreateRequest createRequest = new AdminRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), createRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenAdminApplicationFound_thenReturnAdminApplicationSummaryResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplication adminRegistrationApplication = adminRegisterApplicationEntityToAdminRegisterApplicationMapper
                .map(adminRegisterApplicationEntity);
        AdminRegisterApplicationSummaryResponse adminRegisterApplicationSummaryResponse = adminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegisterApplicationSummaryResponse> mockResponse = AysResponse.successOf(adminRegisterApplicationSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegisterRequest_whenAdminRegistered_thenReturnSuccessResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/complete");
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
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
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
    void givenValidAdminRegisterApplicationId_whenUnauthorizedForApprovingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectingAdminRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .withPermissions(permissionEntities)
                .build();
        UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .withRoles(Set.of(roleEntity))
                .withInstitutionId(institutionEntity.getId())
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegisterApplicationEntity adminRegisterApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegisterApplicationEntity);

        // Given
        String applicationId = adminRegisterApplicationEntity.getId();
        AdminRegisterApplicationRejectRequest rejectRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
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
    void givenValidAdminRegisterApplicationRejectRequest_whenUnauthorizedForRejectingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String applicationId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationRejectRequest rejectRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
