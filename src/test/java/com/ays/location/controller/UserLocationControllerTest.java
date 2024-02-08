package com.ays.location.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import com.ays.location.service.UserLocationService;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

class UserLocationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserLocationService userLocationService;


    private static final String BASE_PATH = "/api/v1/user/location";

    @Test
    void givenValidUserLocationSaveRequest_whenSavedOrUpdatedLocation_thenReturnSuccess() throws Exception {
        // Given
        UserLocationSaveRequest mockUserLocationSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(userLocationService).saveUserLocation(Mockito.any(UserLocationSaveRequest.class));

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, mockUserToken.getAccessToken(), mockUserLocationSaveRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userLocationService, Mockito.times(1))
                .saveUserLocation(Mockito.any(UserLocationSaveRequest.class));
    }

    @Test
    void givenValidUserSupportStatusUpdateRequest_whenAdminRole_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserLocationSaveRequest mockUserLocationSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(userLocationService)
                .saveUserLocation(Mockito.any(UserLocationSaveRequest.class));

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, mockAdminUserToken.getAccessToken(), mockUserLocationSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userLocationService, Mockito.never())
                .saveUserLocation(Mockito.any(UserLocationSaveRequest.class));
    }

}
