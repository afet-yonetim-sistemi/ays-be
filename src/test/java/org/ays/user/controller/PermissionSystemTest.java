package org.ays.user.controller;

import org.ays.AbstractSystemTest;
import org.ays.common.model.response.AysResponse;
import org.ays.user.model.Permission;
import org.ays.user.model.dto.response.PermissionsResponse;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.mapper.PermissionEntityToPermissionMapper;
import org.ays.user.model.mapper.PermissionToPermissionsResponseMapper;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Set;

class PermissionSystemTest extends AbstractSystemTest {

    private final PermissionEntityToPermissionMapper permissionEntityToPermissionMapper = PermissionEntityToPermissionMapper.initialize();
    private final PermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = PermissionToPermissionsResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void whenPermissionsFoundIfUserIsSuperAdmin_thenReturnPermissionsWithSuperPermissions() throws Exception {

        // Initialize
        List<PermissionEntity> permissionEntities = permissionRepository.findAll();
        List<Permission> permissions = permissionEntityToPermissionMapper.map(permissionEntities);

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminTokenV2.getAccessToken());

        List<PermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper.map(permissions);
        AysResponse<List<PermissionsResponse>> response = AysResponse
                .successOf(permissionsResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("size()")
                        .value(response.getResponse().size()));
    }

    @Test
    void whenPermissionsFound_thenReturnPermissions() throws Exception {

        // Initialize
        Set<PermissionEntity> permissionEntities = permissionRepository.findAllByIsSuperFalse();
        List<Permission> permissions = permissionEntityToPermissionMapper.map(permissionEntities);

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminTokenV2.getAccessToken());

        List<PermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper.map(permissions);
        AysResponse<List<PermissionsResponse>> response = AysResponse
                .successOf(permissionsResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("size()")
                        .value(response.getResponse().size()));
    }

}