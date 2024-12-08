package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationBuilder;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToApplicationResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToCreateResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToSummaryResponseMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.auth.model.response.AdminRegistrationApplicationCreateResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationSummaryResponse;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.common.model.AysPageable;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.port.InstitutionSavePort;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.UUIDTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;

class AdminRegistrationApplicationEndToEndTest extends AysEndToEndTest {

    @Autowired
    private InstitutionSavePort institutionSavePort;

    @Autowired
    private AysUserSavePort userSavePort;

    @Autowired
    private AysUserReadPort userReadPort;

    @Autowired
    private AysRoleSavePort roleSavePort;

    @Autowired
    private AysPermissionReadPort permissionReadPort;

    @Autowired
    private AdminRegistrationApplicationSavePort adminRegistrationApplicationSavePort;

    @Autowired
    private AdminRegistrationApplicationReadPort adminRegistrationApplicationReadPort;

    private final AdminRegistrationApplicationToApplicationResponseMapper adminRegistrationApplicationToApplicationResponseMapper = AdminRegistrationApplicationToApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToSummaryResponseMapper adminRegistrationApplicationToSummaryResponseMapper = AdminRegistrationApplicationToSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToCreateResponseMapper adminRegistrationApplicationToCreateResponseMapper = AdminRegistrationApplicationToCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidAdminRegistrationApplicationListRequest_whenAdminRegistrationApplicationsFound_thenReturnAdminRegistrationApplicationsResponse() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutUser()
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        AdminRegistrationApplicationListRequest listRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        AysResponse<AysPageResponse<AdminRegistrationApplication>> mockResponse = AysResponseBuilder.successPage();

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
                        .doesNotHaveJsonPath())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenAdminRegistrationApplicationFound_thenReturnAdminRegistrationApplicationResponse() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .build()
        );
        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );
        AdminRegistrationApplication application = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String applicationId = application.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AdminRegistrationApplicationResponse adminRegistrationApplicationResponse = adminRegistrationApplicationToApplicationResponseMapper
                .map(application);
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
    void givenValidAdminRegistrationApplicationCreateRequest_whenCreatingAdminRegistrationApplication_thenReturnAdminRegistrationApplicationCreateResponse() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        // Given
        AdminRegistrationApplicationCreateRequest createRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
                .withInstitutionId(institution.getId())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), createRequest);

        AdminRegistrationApplication adminRegistrationApplication = new AdminRegistrationApplicationBuilder()
                .withId(AysRandomUtil.generateUUID())
                .build();
        AdminRegistrationApplicationCreateResponse adminRegistrationApplicationCreateResponse = adminRegistrationApplicationToCreateResponseMapper
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
        AysPageable aysPageable = AysPageable.builder()
                .page(1)
                .pageSize(1000)
                .build();
        Optional<AdminRegistrationApplication> registrationApplication = adminRegistrationApplicationReadPort
                .findAll(aysPageable, null).getContent().stream()
                .filter(application -> application.getInstitution().getId().equals(institution.getId()))
                .findFirst();

        Assertions.assertTrue(registrationApplication.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.WAITING, registrationApplication.get().getStatus());
        Assertions.assertEquals(institution.getId(), registrationApplication.get().getInstitution().getId());
        Assertions.assertNull(registrationApplication.get().getUser());
        Assertions.assertTrue(UUIDTestUtil.isValid(registrationApplication.get().getCreatedUser()));
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenAdminApplicationFound_thenReturnAdminApplicationSummaryResponse() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        AdminRegistrationApplication application = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutUser()
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = application.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplicationSummaryResponse adminRegistrationApplicationSummaryResponse = adminRegistrationApplicationToSummaryResponseMapper
                .map(application);
        AysResponse<AdminRegistrationApplicationSummaryResponse> mockResponse = AysResponse.successOf(adminRegistrationApplicationSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(application.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("institution.name")
                        .value(institution.getName()));
    }

    @Test
    void givenValidAdminRegistrationApplicationCompleteRequest_whenAdminRegistered_thenReturnSuccessResponse() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        AdminRegistrationApplication application = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutUser()
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = application.getId();
        AdminRegistrationApplicationCompleteRequest completeRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, completeRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AdminRegistrationApplication> applicationFromDatabase = adminRegistrationApplicationReadPort
                .findById(applicationId);

        Assertions.assertTrue(applicationFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.COMPLETED, applicationFromDatabase.get().getStatus());
        Assertions.assertEquals("AYS", applicationFromDatabase.get().getUpdatedUser());


        List<AysPermission> permissionsFromDatabase = permissionReadPort.findAllByIsSuperFalse();
        Optional<AysUser> userFromDatabase = userReadPort.findByEmailAddress(completeRequest.getEmailAddress());

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(completeRequest.getFirstName(), userFromDatabase.get().getFirstName());
        Assertions.assertEquals(completeRequest.getLastName(), userFromDatabase.get().getLastName());
        Assertions.assertEquals(completeRequest.getCity(), userFromDatabase.get().getCity());
        Assertions.assertEquals(completeRequest.getEmailAddress(), userFromDatabase.get().getEmailAddress());
        Assertions.assertEquals(completeRequest.getPhoneNumber().getCountryCode(), userFromDatabase.get().getPhoneNumber().getCountryCode());
        Assertions.assertEquals(completeRequest.getPhoneNumber().getLineNumber(), userFromDatabase.get().getPhoneNumber().getLineNumber());
        Assertions.assertEquals(AysUserStatus.NOT_VERIFIED, userFromDatabase.get().getStatus());
        Assertions.assertEquals(institution.getId(), userFromDatabase.get().getInstitution().getId());

        Optional<AysRole> userRoleFromDatabase = userFromDatabase.get().getRoles().stream()
                .findFirst();
        Assertions.assertTrue(userRoleFromDatabase.isPresent());

        List<AysPermission> userPermissionsFromDatabase = userRoleFromDatabase.get().getPermissions();
        userPermissionsFromDatabase.forEach(permissions -> Assertions.assertTrue(
                permissionsFromDatabase.stream().anyMatch(
                        userPermission -> userPermission.getId().equals(permissions.getId())
                )
        ));
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenApproveAdminRegistrationApplication_thenReturnNothing() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .build()
        );
        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );
        AdminRegistrationApplication application = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String applicationId = application.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AdminRegistrationApplication> applicationFromDatabase = adminRegistrationApplicationReadPort
                .findById(applicationId);

        Assertions.assertTrue(applicationFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.APPROVED, applicationFromDatabase.get().getStatus());


        Optional<AysUser> userFromDatabase = userReadPort.findById(user.getId());

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(AysUserStatus.ACTIVE, userFromDatabase.get().getStatus());
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenApproveAdminRegistrationApplicationForAlreadyApproved_thenReturnHttp409Conflict() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        List<AysPermission> permissionEntities = permissionReadPort.findAll();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissionEntities)
                        .build()
        );

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withRoles(List.of(role))
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.APPROVED)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplication.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenApproveAdminRegistrationApplicationForWaiting_thenReturnHttp400BadRequest() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutUser()
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplication.getId();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(applicationId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegistrationApplicationId_whenApproveAdminRegistrationApplicationForNonExisting_thenReturnHttp404NotFound() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        List<AysPermission> permissions = permissionReadPort.findAll();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .build()
        );

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );

        adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String someRandomId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(someRandomId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.NOT_FOUND;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isNotFound())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegistrationApplicationRejectRequest_whenRejectingAdminRegistrationApplication_thenReturnNothing() throws Exception {

        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );
        List<AysPermission> permissionEntities = permissionReadPort.findAll();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissionEntities)
                        .build()
        );
        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );
        AdminRegistrationApplication application = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );

        // Given
        String applicationId = application.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), rejectRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AdminRegistrationApplication> applicationFromDatabase = adminRegistrationApplicationReadPort
                .findById(applicationId);

        Assertions.assertTrue(applicationFromDatabase.isPresent());
        Assertions.assertEquals(AdminRegistrationApplicationStatus.REJECTED, applicationFromDatabase.get().getStatus());
        Assertions.assertTrue(UUIDTestUtil.isValid(applicationFromDatabase.get().getUpdatedUser()));
    }

    @Test
    void givenValidAdminRegistrationApplicationRejectRequest_whenRejectAdminRegistrationApplicationForAlreadyRejected_thenReturnHttp409Conflict() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        List<AysPermission> permissionEntities = permissionReadPort.findAll();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissionEntities)
                        .build()
        );

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.REJECTED)
                        .build()
        );


        // Given
        String applicationId = adminRegistrationApplication.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegistrationApplicationRejectRequest_whenRejectAdminRegistrationApplicationForWaiting_thenReturnHttp400BadRequest() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        AdminRegistrationApplication adminRegistrationApplication = adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutUser()
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );

        // Given
        String applicationId = adminRegistrationApplication.getId();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(applicationId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAdminRegistrationApplicationRejectRequest_whenRejectAdminRegistrationApplicationForNonExisting_thenReturnHttp404NotFound() throws Exception {
        // Initialize
        Institution institution = institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .build()
        );

        List<AysPermission> permissionEntities = permissionReadPort.findAll();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withPermissions(permissionEntities)
                        .build()
        );

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withInstitution(institution)
                        .withRoles(List.of(role))
                        .withStatus(AysUserStatus.NOT_VERIFIED)
                        .build()
        );

        adminRegistrationApplicationSavePort.save(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withUser(user)
                        .withInstitution(institution)
                        .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                        .build()
        );


        // Given
        String someRandomId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest rejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(someRandomId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), rejectRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.NOT_FOUND;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isNotFound())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
