package com.ays.backend.user.controller;

import com.ays.backend.base.BaseRestControllerTest;
import com.ays.backend.user.controller.payload.request.SignUpRequestBuilder;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.dto.UserDTOBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        var signUpRequest = new SignUpRequestBuilder().build();
        var userDto = new UserDTOBuilder().withStatus(UserStatus.WAITING).build();
        when(userService.saveUser(signUpRequest)).thenReturn(userDto);

        // when && then
        mockMvc.perform(post(USER_CONTROLLER_BASEURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userDto.getUsername()));
    }

    @Test
    public void shouldReturnGetUserDtoListWithPage() throws Exception {

        int page = 1;
        int pageSize = 10;

        UserDTO userDTO1Info = UserDTO.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .build();

        UserDTO userDTO2Info = UserDTO.builder()
                .username("username 2")
                .firstName("firstname 2")
                .lastName("lastname 2")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .build();

        UserDTO userDTO1 = new UserDTOBuilder()
                .withAllInfo(userDTO1Info)
                .build();

        UserDTO userDTO2 = new UserDTOBuilder()
                .withAllInfo(userDTO2Info)
                .build();

        List<UserDTO> users = Arrays.asList(userDTO1, userDTO2);
        Page<UserDTO> expectedPage = new PageImpl<>(users);

        //when
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(expectedPage);

        // then
        mockMvc.perform(get(USER_CONTROLLER_BASEURL)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].username", is("username 1")))
                .andExpect(jsonPath("$.content[0].firstName", is("firstname 1")))
                .andExpect(jsonPath("$.content[0].lastName", is("lastname 1")))
                .andExpect(jsonPath("$.content[0].userRole", is(UserRole.ROLE_VOLUNTEER.name())))
                .andExpect(jsonPath("$.content[1].username", is("username 2")))
                .andExpect(jsonPath("$.content[1].firstName", is("firstname 2")))
                .andExpect(jsonPath("$.content[1].lastName", is("lastname 2")))
                .andExpect(jsonPath("$.content[1].userRole", is(UserRole.ROLE_VOLUNTEER.name())));


    }

    @Test
    public void shouldReturnGetUserDtoById() throws Exception {

        // given
        Long id = 1L;
        UserDTO expectedUser = UserDTO.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .build();

        // when
        when(userService.getUserById(id)).thenReturn(expectedUser);

        // then
        mockMvc.perform(get("/api/v1/user/{id}", id)
                    .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username 1"))
                .andExpect(jsonPath("$.firstName").value("firstname 1"))
                .andExpect(jsonPath("$.lastName").value("lastname 1"));
    }

}
