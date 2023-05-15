package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminEntityToAdminUserMapper;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.admin_user.service.AdminUserService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserListRequestBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserService adminUserService;

    private final AdminUserToAdminUsersResponseMapper ADMIN_USER_TO_ADMIN_USER_RESPONSE_MAPPER = AdminUserToAdminUsersResponseMapper.initialize();

    private static final AdminEntityToAdminUserMapper ADMIN_USER_ENTITY_TO_ADMIN_USER_MAPPER = AdminEntityToAdminUserMapper.initialize();

    private static final String BASE_PATH = "/api/v1/admin";

    @Test
    void givenValidUserListRequest_whenAdminUsersFound_thenReturnAdminUsersResponse() throws Exception {

        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        // When
        Page<AdminUserEntity> mockAdminUserEntities = new PageImpl<>(
                AdminUserEntityBuilder.generateValidUserEntities(1)
        );
        List<AdminUser> mockAdminUsers = ADMIN_USER_ENTITY_TO_ADMIN_USER_MAPPER.map(mockAdminUserEntities.getContent());
        AysPage<AdminUser> mockAysPageOfUsers = AysPage.of(mockAdminUserEntities, mockAdminUsers);
        Mockito.when(adminUserService.getAllAdminUsers(mockUserListRequest))
                .thenReturn(mockAysPageOfUsers);

        // Then
        List<AdminUsersResponse> mockAdminUsersResponses = ADMIN_USER_TO_ADMIN_USER_RESPONSE_MAPPER.map(mockAysPageOfUsers.getContent());
        AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockAdminUsersResponses)
                .build();
        AysResponse<AysPageResponse<AdminUsersResponse>> mockAysResponse = AysResponse.successOf(pageOfAdminUsersResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").isNotEmpty());

        Mockito.verify(adminUserService, Mockito.times(1))
                .getAllAdminUsers(mockUserListRequest);
    }

    @Test
    void givenValidUserListRequest_whenAdminUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, mockUserToken.getAccessToken(), mockUserListRequest);

        AysResponse<AysError> mockResponse = AysResponseBuilder.UNAUTHORIZED;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isUnauthorized())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }
}