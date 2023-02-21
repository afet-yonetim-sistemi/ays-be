package com.ays.backend.user.controller;

import com.ays.backend.base.BaseRestControllerTest;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.service.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
    private UserControllerService userControllerService;

    @Test
    void shouldReturnCreatedOnValidInput() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder().build();
        var userUUID = "useruuid";
        var userDto = UserDTO.builder().userUUID(userUUID).build();
        when(userControllerService.createUser(signUpRequest)).thenReturn(userDto);

        // when && then
        mockMvc.perform(post(USER_CONTROLLER_BASEURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userUUID));
    }

}
