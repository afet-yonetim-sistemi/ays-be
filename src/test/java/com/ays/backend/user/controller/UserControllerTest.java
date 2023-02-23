package com.ays.backend.user.controller;

import com.ays.backend.base.BaseRestControllerTest;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseRestControllerTest {
    private final String USER_CONTROLLER_BASEURL = "/api/v1/user";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnCreatedOnValidInput() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("testUser")
                .password("password")
                .countryCode(90)
                .lineNumber(123456789)
                .statusId(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        var username = "username123";
        var userDto = UserDTO.builder()
                .userStatus(UserStatus.WAITING)
                .username(username).build();
        when(userService.saveUser(signUpRequest)).thenReturn(userDto);

        // when && then
        mockMvc.perform(post(USER_CONTROLLER_BASEURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userDto.getUsername()));
    }

}
