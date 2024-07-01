package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleToRolesSummaryResponseMapper;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleCreateRequestBuilder;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.request.AysRoleListRequestBuilder;
import org.ays.auth.model.response.AysRolesResponse;
import org.ays.auth.model.response.AysRolesSummaryResponse;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class AysRoleEndToEndTest extends AysEndToEndTest {

    @Autowired
    private AysPermissionReadPort permissionReadPort;

    @Autowired
    private AysRoleReadPort roleReadPort;


    private final AysRoleToRolesSummaryResponseMapper roleToRolesSummaryResponseMapper = AysRoleToRolesSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void whenRolesFound_thenReturnRoles() throws Exception {

        // Initialize
        Set<AysRole> roles = roleReadPort.findAll();

        // Then
        String endpoint = BASE_PATH.concat("/roles/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        List<AysRolesSummaryResponse> rolesSummaryResponses = roleToRolesSummaryResponseMapper.map(roles);
        AysResponse<List<AysRolesSummaryResponse>> response = AysResponse
                .successOf(rolesSummaryResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("size()")
                        .value(response.getResponse().size()));
    }


    @Test
    void givenValidRoleListRequest_whenRolesFoundForSuperAdmin_thenReturnAysPageResponseOfRolesResponse() throws Exception {

        // Given
        AysRoleListRequest listRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withName("AYS Yetkilisi")
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
        createRequest.getPermissionIds().forEach(permissionId -> {
            Assertions.assertTrue(
                    role.get().getPermissions().stream()
                            .anyMatch(permission -> permission.getId().equals(permissionId))
            );
        });
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
        createRequest.getPermissionIds().forEach(permissionId -> {
            Assertions.assertTrue(
                    role.get().getPermissions().stream()
                            .anyMatch(permission -> permission.getId().equals(permissionId))
            );
        });
    }

}
