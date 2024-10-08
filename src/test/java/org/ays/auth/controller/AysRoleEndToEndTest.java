package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleToResponseMapper;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleCreateRequestBuilder;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.request.AysRoleListRequestBuilder;
import org.ays.auth.model.request.AysRoleUpdateRequest;
import org.ays.auth.model.request.AysRoleUpdateRequestBuilder;
import org.ays.auth.model.response.AysRoleResponse;
import org.ays.auth.model.response.AysRolesResponse;
import org.ays.auth.model.response.AysRolesSummaryResponse;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.response.AysErrorResponseBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class AysRoleEndToEndTest extends AysEndToEndTest {

    @Autowired
    private AysRoleSavePort roleSavePort;

    @Autowired
    private AysRoleReadPort roleReadPort;

    @Autowired
    private AysPermissionReadPort permissionReadPort;


    private final AysRoleToResponseMapper roleToResponseMapper = AysRoleToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void whenRolesFound_thenReturnSummaryOfRoles() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/roles/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        AysResponse<List<AysRolesSummaryResponse>> response = AysResponseBuilder.successList();

        ResultActions resultActions = aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.responses("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.responses("name")
                        .isNotEmpty());

        // Verify
        List<AysRole> roles = roleReadPort
                .findAllActivesByInstitutionId(AysValidTestData.Admin.INSTITUTION_ID);

        resultActions.andExpect(AysMockResultMatchersBuilders.responseSize()
                .value(roles.size()));

        for (AysRole role : roles) {
            int index = roles.indexOf(role);
            resultActions
                    .andExpect(AysMockResultMatchersBuilders.response(index, "id")
                            .value(role.getId()))
                    .andExpect(AysMockResultMatchersBuilders.response(index, "name")
                            .value(role.getName()));
        }

    }


    @ParameterizedTest
    @ValueSource(strings = {
            "AYS Yetkilisi",
            "Öğretici"
    })
    void givenValidRoleListRequest_whenRolesFoundForSuperAdmin_thenReturnAysPageResponseOfRolesResponse(String name) throws Exception {

        // Given
        AysRoleListRequest listRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withName(name)
                .withStatuses(Set.of(AysRoleStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysRolesResponse>> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }

    @Test
    void givenValidRoleListRequest_whenRolesFoundForAdmin_thenReturnAysPageResponseOfRolesResponse() throws Exception {

        // Given
        AysRoleListRequest listRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withName("Kurum Yöneticisi")
                .withStatuses(Set.of(AysRoleStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysRolesResponse>> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }

    @Test
    void givenValidRoleListRequest_whenRoleFoundWithName_thenReturnAysPageResponseOfRolesResponse() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();
        roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("Öğretici")
                        .withStatus(AysRoleStatus.ACTIVE)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.SuperAdmin.INSTITUTION_ID).build())
                        .withPermissions(permissions)
                        .build()
        );

        // Given
        AysRoleListRequest listRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withName("Öğretici")
                .withStatuses(Set.of(AysRoleStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysRolesResponse>> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .value(listRequest.getFilter().getName()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }


    @Test
    void givenValidRoleId_whenRoleExists_thenReturnRoleResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("öğreticiRolü")
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .build()
        );

        // Given
        String roleId = role.getId();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(roleId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        AysRoleResponse mockRoleResponse = roleToResponseMapper
                .map(role);

        AysResponse<AysRoleResponse> mockResponse = AysResponse
                .successOf(mockRoleResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .value(role.getCreatedUser()))
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(role.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("name")
                        .value(role.getName()))
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("permissions[*].id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("permissions[*].name")
                        .isNotEmpty());
    }


    @Test
    void givenValidRoleCreateRequest_whenSuperRoleCreated_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());

        // Given
        AysRoleCreateRequest createRequest = new AysRoleCreateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), createRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> role = roleReadPort.findByName(createRequest.getName());

        Assertions.assertTrue(role.isPresent());
        Assertions.assertNotNull(role.get().getId());
        Assertions.assertNotNull(role.get().getInstitution());
        Assertions.assertEquals(createRequest.getName(), role.get().getName());
        Assertions.assertEquals(AysRoleStatus.ACTIVE, role.get().getStatus());
        createRequest.getPermissionIds().forEach(permissionId -> Assertions.assertTrue(
                role.get().getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(permissionId))
        ));
    }

    @Test
    void givenValidRoleCreateRequest_whenRoleCreated_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());

        // Given
        AysRoleCreateRequest createRequest = new AysRoleCreateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), createRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> role = roleReadPort.findByName(createRequest.getName());

        Assertions.assertTrue(role.isPresent());
        Assertions.assertNotNull(role.get().getId());
        Assertions.assertNotNull(role.get().getInstitution());
        Assertions.assertEquals(createRequest.getName(), role.get().getName());
        Assertions.assertEquals(AysRoleStatus.ACTIVE, role.get().getStatus());
        createRequest.getPermissionIds().forEach(permissionId -> Assertions.assertTrue(
                role.get().getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(permissionId))
        ));
    }

    @Test
    void givenRoleCreateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenReturnForbiddenError() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());

        // Given
        AysRoleCreateRequest createRequest = new AysRoleCreateRequestBuilder()
                .withName(AysRandomUtil.generateText(10))
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), createRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotHaveJsonPath());

        // Verify
        Optional<AysRole> role = roleReadPort.findByName(createRequest.getName());

        Assertions.assertFalse(role.isPresent());
    }


    @Test
    void givenValidIdAndRoleUpdateRequest_whenSuperRoleUpdated_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());

        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("Admin Role 1")
                        .withPermissions(permissions)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.SuperAdmin.INSTITUTION_ID).build())
                        .build()
        );

        // Given
        String id = role.getId();
        AysRoleUpdateRequest updateRequest = new AysRoleUpdateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, superAdminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertNotNull(roleFromDatabase.get().getId());
        Assertions.assertNotNull(roleFromDatabase.get().getInstitution());
        Assertions.assertEquals(updateRequest.getName(), roleFromDatabase.get().getName());
        Assertions.assertEquals(AysRoleStatus.ACTIVE, roleFromDatabase.get().getStatus());
        updateRequest.getPermissionIds().forEach(permissionId -> Assertions.assertTrue(
                roleFromDatabase.get().getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(permissionId))
        ));
    }

    @Test
    void givenValidRoleUpdateRequest_whenRoleUpdated_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());


        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("Admin Role 2")
                        .withPermissions(permissions)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.Admin.INSTITUTION_ID).build())
                        .build()
        );

        // Given
        String id = role.getId();
        AysRoleUpdateRequest updateRequest = new AysRoleUpdateRequestBuilder()
                .withName("Updated Role")
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertNotNull(roleFromDatabase.get().getId());
        Assertions.assertNotNull(roleFromDatabase.get().getInstitution());
        Assertions.assertEquals(updateRequest.getName(), roleFromDatabase.get().getName());
        Assertions.assertEquals(AysRoleStatus.ACTIVE, roleFromDatabase.get().getStatus());
        updateRequest.getPermissionIds().forEach(permissionId -> Assertions.assertTrue(
                roleFromDatabase.get().getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(permissionId))
        ));
    }

    @Test
    void givenValidRoleUpdateRequest_whenPermissionsUpdatedAndNameUnchanged_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());


        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("Admin Role")
                        .withPermissions(permissions)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.Admin.INSTITUTION_ID).build())
                        .build()
        );

        // Given
        String id = role.getId();
        AysRoleUpdateRequest updateRequest = new AysRoleUpdateRequestBuilder()
                .withName("Admin Role")
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertNotNull(roleFromDatabase.get().getId());
        Assertions.assertNotNull(roleFromDatabase.get().getInstitution());
        Assertions.assertEquals(updateRequest.getName(), roleFromDatabase.get().getName());
        Assertions.assertEquals(AysRoleStatus.ACTIVE, roleFromDatabase.get().getStatus());
        updateRequest.getPermissionIds().forEach(permissionId -> Assertions.assertTrue(
                roleFromDatabase.get().getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(permissionId))
        ));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenRequestHasSuperPermissionsAndUserIsNotSuperAdmin_thenReturnForbiddenError() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();
        Set<String> permissionIds = permissions.stream()
                .map(AysPermission::getId)
                .collect(Collectors.toSet());

        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName(AysRandomUtil.generateText(10))
                        .withPermissions(permissions)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.Admin.INSTITUTION_ID).build())
                        .build()
        );

        // Given
        String id = role.getId();
        AysRoleUpdateRequest updateRequest = new AysRoleUpdateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotHaveJsonPath());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertNotEquals(updateRequest.getName(), roleFromDatabase.get().getName());
        Assertions.assertNull(roleFromDatabase.get().getUpdatedUser());
        Assertions.assertNull(roleFromDatabase.get().getUpdatedAt());
    }


    @Test
    void givenId_whenRoleActivated_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("buBirRol")
                        .withStatus(AysRoleStatus.PASSIVE)
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .build()
        );

        // Given
        String id = role.getId();

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(id).concat("/activate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertEquals(roleFromDatabase.get().getId(), id);
        Assertions.assertEquals(AysRoleStatus.ACTIVE, roleFromDatabase.get().getStatus());
    }


    @Test
    void givenId_whenRolePassivated_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();
        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("rastgeleBirRol")
                        .withInstitution(institution)
                        .withPermissions(permissions)
                        .withStatus(AysRoleStatus.ACTIVE)
                        .build()
        );

        // Given
        String id = role.getId();

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(id).concat("/passivate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertEquals(roleFromDatabase.get().getId(), id);
        Assertions.assertEquals(AysRoleStatus.PASSIVE, roleFromDatabase.get().getStatus());
    }


    @Test
    void givenValidId_whenRoleDeleted_thenReturnSuccess() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();

        AysRole role = roleSavePort.save(
                new AysRoleBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("eğitmenRolü")
                        .withPermissions(permissions)
                        .withInstitution(new InstitutionBuilder().withId(AysValidTestData.Admin.INSTITUTION_ID).build())
                        .build()
        );

        // Given
        String id = role.getId();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysRole> roleFromDatabase = roleReadPort.findById(id);

        Assertions.assertTrue(roleFromDatabase.isPresent());
        Assertions.assertNotNull(roleFromDatabase.get().getId());
        Assertions.assertEquals(AysRoleStatus.DELETED, roleFromDatabase.get().getStatus());
    }

}
