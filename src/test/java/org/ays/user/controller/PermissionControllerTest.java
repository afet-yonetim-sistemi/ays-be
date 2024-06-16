package org.ays.user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.response.AysResponse;
import org.ays.user.model.Permission;
import org.ays.user.model.PermissionBuilder;
import org.ays.user.model.mapper.PermissionToPermissionsResponseMapper;
import org.ays.user.model.response.PermissionsResponse;
import org.ays.user.service.PermissionService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class PermissionControllerTest extends AbstractRestControllerTest {

    @MockBean
    private PermissionService permissionService;


    private final PermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = PermissionToPermissionsResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void whenPermissionsFound_thenReturnPermissions() throws Exception {

        // When
        List<Permission> mockPermissions = List.of(
                new PermissionBuilder().withValidFields().build(),
                new PermissionBuilder().withValidFields().build()
        );

        Mockito.when(permissionService.findAll())
                .thenReturn(mockPermissions);

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminTokenV2.getAccessToken());

        List<PermissionsResponse> mockPermissionsResponses = permissionToPermissionsResponseMapper.map(mockPermissions);
        AysResponse<List<PermissionsResponse>> mockResponse = AysResponse
                .successOf(mockPermissionsResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(permissionService, Mockito.times(1))
                .findAll();
    }

}