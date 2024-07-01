package org.ays.auth.controller;


import org.ays.AysRestControllerTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.model.response.AysUserListResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AysUserControllerTest extends AysRestControllerTest {

    @MockBean
    private AysUserReadService userReadService;


    private final AysUserToUsersResponseMapper userToUsersResponseMapper = AysUserToUsersResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidUserListRequest_whenUsersFound_thenReturnAysPageResponseOfUsersResponse() throws Exception {
        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .build();

        //When
        List<AysUser> mockUsers = List.of(
                new AysUserBuilder().withValidValues().build()
        );

        AysPage<AysUser> mockUserPage = AysPageBuilder
                .from(mockUsers, mockListRequest.getPageable());

        Mockito.when(userReadService.findAll(Mockito.any(AysUserListRequest.class)))
                .thenReturn(mockUserPage);

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AysUserListResponse> mockUsersResponse = userToUsersResponseMapper.map(mockUsers);
        AysPageResponse<AysUserListResponse> pageOfResponse = AysPageResponse.<AysUserListResponse>builder()
                .of(mockUserPage)
                .content(mockUsersResponse)
                .build();
        AysResponse<AysPageResponse<AysUserListResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.times(1))
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenFirstNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withFirstName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenLastNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "City user 1234",
            "City *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenCityDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withCity(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

}