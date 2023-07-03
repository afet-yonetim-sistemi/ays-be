package com.ays.admin_user.controller;

import java.util.List;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.dto.request.AdminUserListRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.admin_user.service.AdminUserService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserService adminUserService;


    private static final AdminUserToAdminUsersResponseMapper ADMIN_USER_TO_ADMIN_USER_RESPONSE_MAPPER = AdminUserToAdminUsersResponseMapper.initialize();

    private static final AdminUserEntityToAdminUserMapper ADMIN_USER_ENTITY_TO_ADMIN_USER_MAPPER = AdminUserEntityToAdminUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admins";

    @Test
    void givenValidUserListRequest_whenAdminUsersFound_thenReturnAdminUsersResponse() throws Exception {

        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        // When
        Page<AdminUserEntity> mockAdminUserEntities = new PageImpl<>(
                AdminUserEntityBuilder.generateValidUserEntities(1)
        );
        List<AdminUser> mockAdminUsers = ADMIN_USER_ENTITY_TO_ADMIN_USER_MAPPER.map(mockAdminUserEntities.getContent());
        AysPage<AdminUser> mockAysPageOfUsers = AysPage.of(mockAdminUserEntities, mockAdminUsers);
        Mockito.when(adminUserService.getAdminUsers(mockAdminUserListRequest))
                .thenReturn(mockAysPageOfUsers);

        // Then
        List<AdminUsersResponse> mockAdminUsersResponses = ADMIN_USER_TO_ADMIN_USER_RESPONSE_MAPPER.map(mockAysPageOfUsers.getContent());
        AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockAdminUsersResponses)
                .build();

        AysResponse<AysPageResponse<AdminUsersResponse>> mockAysResponse = AysResponse.successOf(pageOfAdminUsersResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH, mockAdminUserToken.getAccessToken(), mockAdminUserListRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().isNotEmpty());

        Mockito.verify(adminUserService, Mockito.times(1))
                .getAdminUsers(mockAdminUserListRequest);
    }

    @Test
    void givenValidUserListRequest_whenAdminUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder().withValidValues().build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, mockUserToken.getAccessToken(), mockAdminUserListRequest);

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

}
