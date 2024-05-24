package org.ays.user.controller;

import org.ays.AbstractSystemTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.AdminRegistrationApplication;
import org.ays.user.model.AdminRegistrationApplicationBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.user.model.dto.response.AdminRegistrationApplicationCreateResponse;
import org.ays.user.model.dto.response.AdminRegistrationApplicationResponse;
import org.ays.user.model.dto.response.AdminRegistrationApplicationSummaryResponse;
import org.ays.user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.RoleEntityBuilder;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.mapper.AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AdminRegistrationApplicationSystemTest extends AbstractSystemTest {

    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper.initialize();


    private void initialize(InstitutionEntity institutionEntity) {
        institutionRepository.save(institutionEntity);
    }

    private void initialize(InstitutionEntity institutionEntity,
                            RoleEntity roleEntity,
                            UserEntityV2 userEntity,
                            AdminRegistrationApplicationEntity adminRegistrationApplicationEntity) {

        institutionRepository.save(institutionEntity);
        roleRepository.save(roleEntity);
        userRepositoryV2.save(userEntity);
        adminRegistrationApplicationRepository.save(adminRegistrationApplicationEntity);
    }


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidAdminRegisterApplicationListRequest_whenAdminRegisterApplicationsFound_thenReturnAdminRegisterApplicationsResponse() throws Exception {

        // Given
        AdminRegistrationApplicationListRequest listRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), listRequest);

        List<AdminRegistrationApplicationEntity> adminRegisterApplicationEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().withStatus(AdminRegistrationApplicationStatus.WAITING).build()
        );
        Page<AdminRegistrationApplicationEntity> pageOfEntities = new PageImpl<>(
                adminRegisterApplicationEntities
        );
        List<AdminRegistrationApplication> adminRegistrationApplications = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper
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
        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegistrationApplicationEntity);

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();

        // When
        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper
                .map(adminRegistrationApplicationEntity);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminTokenV2.getAccessToken());

        AdminRegistrationApplicationResponse adminRegistrationApplicationResponse = adminRegistrationApplicationToAdminRegistrationApplicationResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegistrationApplicationResponse> mockResponse = AysResponse
                .successOf(adminRegistrationApplicationResponse);

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
    void givenValidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnAdminRegisterApplicationCreateResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        this.initialize(institutionEntity);

        // Given
        AdminRegistrationApplicationCreateRequest createRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(institutionEntity.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), createRequest);

        AdminRegistrationApplication adminRegistrationApplication = new AdminRegistrationApplicationBuilder()
                .withId(AysRandomUtil.generateUUID())
                .build();
        AdminRegistrationApplicationCreateResponse adminRegistrationApplicationCreateResponse = adminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegistrationApplicationCreateResponse> mockResponse = AysResponse
                .successOf(adminRegistrationApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
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
        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegistrationApplicationEntity);

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper
                .map(adminRegistrationApplicationEntity);
        AdminRegistrationApplicationSummaryResponse adminRegistrationApplicationSummaryResponse = adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegistrationApplicationSummaryResponse> mockResponse = AysResponse.successOf(adminRegistrationApplicationSummaryResponse);

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
        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegistrationApplicationEntity);

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
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
        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegistrationApplicationEntity);

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
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
        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUserId(userEntity.getId())
                .withInstitutionId(userEntity.getInstitutionId())
                .build();
        this.initialize(institutionEntity, roleEntity, userEntity, adminRegistrationApplicationEntity);

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), rejectRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
