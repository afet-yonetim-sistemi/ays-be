package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.mapper.AysPermissionToPermissionsResponseMapper;
import org.ays.auth.model.response.AysPermissionsResponse;
import org.ays.auth.port.AysPermissionReadPort;
import org.ays.common.model.response.AysResponse;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AysPermissionEndToEndTest extends AysEndToEndTest {

    @Autowired
    private AysPermissionReadPort permissionReadPort;


    private final AysPermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = AysPermissionToPermissionsResponseMapper.initialize();


    private static final String BASE_PATH = "/api/institution/v1";


    @Test
    void whenPermissionsFoundIfUserIsSuperAdmin_thenReturnPermissionsWithSuperPermissionsWithoutLandingPagePermission() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAll();

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        List<AysPermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper.map(permissions);
        AysResponse<List<AysPermissionsResponse>> response = AysResponse
                .successOf(permissionsResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("size()")
                        .value(response.getResponse().size() - 1))
                .andExpect(AysMockResultMatchersBuilders.responses("name")
                        .value(Matchers.not("landing:page")));
    }

    @Test
    void whenPermissionsFound_thenReturnPermissionsWithoutLandingPagePermission() throws Exception {

        // Initialize
        List<AysPermission> permissions = permissionReadPort.findAllByIsSuperFalse();

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        List<AysPermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper.map(permissions);
        AysResponse<List<AysPermissionsResponse>> response = AysResponse
                .successOf(permissionsResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, response)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("size()")
                        .value(response.getResponse().size() - 1))
                .andExpect(AysMockResultMatchersBuilders.responses("name")
                        .value(Matchers.not("landing:page")));
    }

}
