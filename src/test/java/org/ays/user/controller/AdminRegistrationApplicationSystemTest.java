package org.ays.user.controller;

import org.ays.AbstractSystemTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class AdminRegistrationApplicationSystemTest extends AbstractSystemTest {

    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper.initialize();


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
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAllByIsSuperFalse());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );
        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .build()
        );
        AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();

        // When
        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper
                .map(applicationEntity);

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
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

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
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .isNotEmpty());

        // Verify
        Optional<AdminRegistrationApplicationEntity> applicationEntityFromDatabase = adminRegistrationApplicationRepository
                .findAll().stream()
                .filter(applicationEntity -> applicationEntity.getInstitutionId().equals(institutionEntity.getId()))
                .findFirst();

        Assertions.assertTrue(applicationEntityFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.WAITING, applicationEntityFromDatabase.get().getStatus());
        Assertions.assertEquals(institutionEntity.getId(), applicationEntityFromDatabase.get().getInstitutionId());
        Assertions.assertNull(applicationEntityFromDatabase.get().getUserId());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenAdminApplicationFound_thenReturnAdminApplicationSummaryResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );
        AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper
                .map(applicationEntity);
        AdminRegistrationApplicationSummaryResponse adminRegistrationApplicationSummaryResponse = adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper
                .map(adminRegistrationApplication);
        AysResponse<AdminRegistrationApplicationSummaryResponse> mockResponse = AysResponse.successOf(adminRegistrationApplicationSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(applicationEntity.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("institution.name")
                        .value(institutionEntity.getName()));
    }

    @Test
    void givenValidAdminRegisterRequest_whenAdminRegistered_thenReturnSuccessResponse() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );
        AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();
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

        // Verify
        Optional<AdminRegistrationApplicationEntity> applicationEntityFromDatabase = adminRegistrationApplicationRepository
                .findById(applicationId);

        Assertions.assertTrue(applicationEntityFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.COMPLETED, applicationEntityFromDatabase.get().getStatus());


        Set<PermissionEntity> permissionEntitiesFromDatabase = new HashSet<>(permissionRepository.findAllByIsSuperFalse());
        Optional<UserEntityV2> userEntityFromDatabase = userRepositoryV2.findByEmailAddress(completeRequest.getEmailAddress());

        Assertions.assertTrue(userEntityFromDatabase.isPresent());
        Assertions.assertEquals(completeRequest.getFirstName(), userEntityFromDatabase.get().getFirstName());
        Assertions.assertEquals(completeRequest.getLastName(), userEntityFromDatabase.get().getLastName());
        Assertions.assertEquals(completeRequest.getCity(), userEntityFromDatabase.get().getCity());
        Assertions.assertEquals(completeRequest.getEmailAddress(), userEntityFromDatabase.get().getEmailAddress());
        Assertions.assertEquals(completeRequest.getPhoneNumber().getCountryCode(), userEntityFromDatabase.get().getCountryCode());
        Assertions.assertEquals(completeRequest.getPhoneNumber().getLineNumber(), userEntityFromDatabase.get().getLineNumber());
        Assertions.assertEquals(UserStatus.NOT_VERIFIED, userEntityFromDatabase.get().getStatus());
        Assertions.assertEquals(institutionEntity.getId(), userEntityFromDatabase.get().getInstitutionId());

        Optional<RoleEntity> userRoleEntityFromDatabase = userEntityFromDatabase.get().getRoles().stream()
                .findFirst();
        Assertions.assertTrue(userRoleEntityFromDatabase.isPresent());

        Set<PermissionEntity> userPermissionEntitiesFromDatabase = userRoleEntityFromDatabase.get().getPermissions();
        permissionEntitiesFromDatabase.forEach(permissionEntity -> Assertions.assertTrue(
                userPermissionEntitiesFromDatabase.stream().anyMatch(
                        userPermissionEntity -> userPermissionEntity.getId().equals(permissionEntity.getId())
                )
        ));
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAllByIsSuperFalse());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );
        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );
        AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();

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

        // Verify
        Optional<AdminRegistrationApplicationEntity> applicationEntityFromDatabase = adminRegistrationApplicationRepository
                .findById(applicationId);

        Assertions.assertTrue(applicationEntityFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.VERIFIED, applicationEntityFromDatabase.get().getStatus());


        Optional<UserEntityV2> userEntityFromDatabase = userRepositoryV2.findById(userEntity.getId());

        Assertions.assertTrue(userEntityFromDatabase.isPresent());
        Assertions.assertEquals(UserStatus.ACTIVE, userEntityFromDatabase.get().getStatus());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplicationForAlreadyApproved_thenReturnHttp409Conflict() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );

        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withRoles(Set.of(roleEntity))
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );

        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.VERIFIED)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplicationForWaiting_thenReturnHttp400BadRequest() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );

        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );

        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.BAD_REQUEST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplicationForNonExisting_thenReturnHttp404NotFound() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );

        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );

        adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String someRandomId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(someRandomId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.NOT_FOUND;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isNotFound())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectingAdminRegisterApplication_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAllByIsSuperFalse());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );
        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );
        AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();
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

        // Verify
        Optional<AdminRegistrationApplicationEntity> applicationEntityFromDatabase = adminRegistrationApplicationRepository
                .findById(applicationId);

        Assertions.assertTrue(applicationEntityFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.REJECTED, applicationEntityFromDatabase.get().getStatus());
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectAdminRegisterApplicationForAlreadyRejected_thenReturnHttp409Conflict() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );

        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withRoles(Set.of(roleEntity))
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );

        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.REJECTED)
                        .build()
        );


        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectAdminRegisterApplicationForWaiting_thenReturnHttp400BadRequest() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        AdminRegistrationApplicationEntity adminRegistrationApplicationEntity = adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplicationEntity.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.BAD_REQUEST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectAdminRegisterApplicationForNonExisting_thenReturnHttp404NotFound() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .build()
        );

        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        RoleEntity roleEntity = roleRepository.save(
                new RoleEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withPermissions(permissionEntities)
                        .build()
        );

        UserEntityV2 userEntity = userRepositoryV2.save(
                new UserEntityV2Builder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(institutionEntity.getId())
                        .withRoles(Set.of(roleEntity))
                        .withStatus(UserStatus.NOT_VERIFIED)
                        .build()
        );

        adminRegistrationApplicationRepository.save(
                new AdminRegisterApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withUserId(userEntity.getId())
                        .withInstitutionId(institutionEntity.getId())
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );


        // Given
        String someRandomId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(someRandomId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.NOT_FOUND;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isNotFound())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
