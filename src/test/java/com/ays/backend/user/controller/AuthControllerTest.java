package com.ays.backend.user.controller;

import com.ays.backend.base.BaseRestControllerTest;
import com.ays.backend.user.controller.payload.request.*;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.model.Token;
import com.ays.backend.user.model.User;
import com.ays.backend.user.model.UserBuilder;
import com.ays.backend.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends BaseRestControllerTest {

    private final String ADMIN_CONTROLLER_BASEURL = "/api/v1/admin";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @Test
    void shouldRegisterForAdmin() throws Exception {

        // Given
        AdminRegisterRequest registerRequest = new AdminRegisterRequestBuilder().build();

        User user = new UserBuilder()
                .withRegisterRequest(registerRequest).build();

        MessageResponse messageResponse = new MessageResponse("success");

        // when
        when(authService.register(registerRequest)).thenReturn(user);

        // then - Perform the POST request and assert the response
        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(messageResponse.getMessage()));

    }

    @Test
    void shouldLoginForAdmin() throws Exception {

        // given
        AdminLoginRequest loginRequest = new AdminLoginRequestBuilder().build();

        Token aysToken = Token.builder()
                .accessTokenExpireIn(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("Bearer access-token")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpireIn(aysToken.getAccessTokenExpireIn())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        // when
        when(authService.login(loginRequest)).thenReturn(aysToken);


        // then
        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authResponse.getAccessToken()))
                .andExpect(jsonPath("$.accessTokenExpireIn").value(authResponse.getAccessTokenExpireIn()))
                .andExpect(jsonPath("$.refreshToken").value(authResponse.getRefreshToken()));

    }

    @Test
    void shouldRefreshTokenForAdmin() throws Exception {

        // given
        AdminRefreshTokenRequest refreshTokenRequest = new AdminRefreshTokenRequestBuilder().build();


        Token aysToken = Token.builder()
                .accessTokenExpireIn(new Date().getTime() + 120000)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .accessToken("Bearer access-token")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpireIn(aysToken.getAccessTokenExpireIn())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        // when
        when(authService.refreshToken(refreshTokenRequest)).thenReturn(aysToken);

        // then
        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/refresh-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authResponse.getAccessToken()))
                .andExpect(jsonPath("$.accessTokenExpireIn").value(authResponse.getAccessTokenExpireIn()))
                .andExpect(jsonPath("$.refreshToken").value(authResponse.getRefreshToken()));
    }
}
