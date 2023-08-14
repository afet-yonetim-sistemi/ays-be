package com.ays.admin_user.controller;

import com.ays.AbstractSystemTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.dto.request.AdminUserListRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserSystemTest extends AbstractSystemTest {

    private final AdminUserToAdminUsersResponseMapper adminUserToAdminUserResponseMapper = AdminUserToAdminUsersResponseMapper.initialize();

    private final AdminUserEntityToAdminUserMapper adminUserEntityToAdminUserMapper = AdminUserEntityToAdminUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidUserListRequest_whenAdminUsersFound_thenReturnAdminUsersResponse() throws Exception {

        // Given
        AdminUserListRequest adminUserListRequest = new AdminUserListRequestBuilder()
                .withValidValues()
                .build();

        // When
        Page<AdminUserEntity> adminUserEntities = new PageImpl<>(
                AdminUserEntityBuilder.generateValidUserEntities(1)
        );
        List<AdminUser> adminUsers = adminUserEntityToAdminUserMapper.map(adminUserEntities.getContent());
        AysPage<AdminUser> pageOfUsers = AysPage.of(adminUserEntities, adminUsers);

        // Then
        List<AdminUsersResponse> adminUsersResponses = adminUserToAdminUserResponseMapper.map(pageOfUsers.getContent());
        AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(pageOfUsers)
                .content(adminUsersResponses)
                .build();

        AysResponse<AysPageResponse<AdminUsersResponse>> response = AysResponse.successOf(pageOfAdminUsersResponse);
        String endpoint = BASE_PATH.concat("/admins");
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), adminUserListRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().isNotEmpty());
    }

    @Test
    void givenValidUserListRequest_whenAdminUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminUserListRequest adminUserListRequest = new AdminUserListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admins");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), adminUserListRequest);

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
