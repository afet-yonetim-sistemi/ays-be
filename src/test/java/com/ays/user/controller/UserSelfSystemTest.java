package com.ays.user.controller;

import com.ays.AbstractSystemTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class UserSelfSystemTest extends AbstractSystemTest {

    private static final String BASE_PATH = "/api/v1/user-self";

    @Test
    void givenValidUserSupportStatusUpdateRequest_whenUserRole_thenReturnSuccess() throws Exception {
        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUserSupportStatusUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // Then
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(BASE_PATH.concat("/status/support"), userTokenTwo.getAccessToken(), mockUserSupportStatusUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidUserSupportStatusUpdateRequest_whenAdminRole_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUserSupportStatusUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(BASE_PATH.concat("/status/support"), adminUserTokenOne.getAccessToken(), mockUserSupportStatusUpdateRequest);

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
