package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Set;

class AysUserEndToEndTest extends AysEndToEndTest {

    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidUserListRequest_whenUsersFoundForSuperAdmin_thenReturnAysPageResponseOfUsersResponse() throws Exception {

        // Given
        AysUserListRequest listRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponseBuilder.success();

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
                .andExpect(AysMockResultMatchersBuilders.firstContent("emailAddress")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("city")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }

    @Test
    void givenValidUserListRequest_whenUsersFoundForAdmin_thenReturnAysPageResponseOfUsersResponse() throws Exception {

        // Given
        AysUserListRequest listRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .withCity("Mersin")
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponseBuilder.success();

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
                .andExpect(AysMockResultMatchersBuilders.firstContent("emailAddress")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("city")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }

}
