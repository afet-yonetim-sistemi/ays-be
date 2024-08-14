package org.ays.auth.controller;

import org.ays.AysRestControllerTest;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.AysPermissionBuilder;
import org.ays.auth.model.mapper.AysPermissionToPermissionsResponseMapper;
import org.ays.auth.model.response.AysPermissionsResponse;
import org.ays.auth.service.AysPermissionService;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AysPermissionControllerTest extends AysRestControllerTest {

    @MockBean
    private AysPermissionService permissionService;


    private final AysPermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = AysPermissionToPermissionsResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void whenPermissionsFound_thenReturnPermissions() throws Exception {

        // When
        List<AysPermission> mockPermissions = List.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build()
        );

        Mockito.when(permissionService.findAll())
                .thenReturn(mockPermissions);

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        List<AysPermissionsResponse> mockPermissionsResponses = permissionToPermissionsResponseMapper.map(mockPermissions);
        AysResponse<List<AysPermissionsResponse>> mockResponse = AysResponse
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

    @Test
    void whenUnauthorizedForFoundingPermissions_thenReturnAccessDeniedException() throws Exception {

        // When
        List<AysPermission> mockPermissions = List.of(
                new AysPermissionBuilder().withValidValues().build(),
                new AysPermissionBuilder().withValidValues().build()
        );

        Mockito.when(permissionService.findAll())
                .thenReturn(mockPermissions);

        // Then
        String endpoint = BASE_PATH.concat("/permissions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(permissionService, Mockito.never())
                .findAll();
    }

}